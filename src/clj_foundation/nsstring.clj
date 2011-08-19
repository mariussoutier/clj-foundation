(ns clj-foundation.nsstring
  (:require [clojure.contrib.string :as sutil]))

;; My own String utility functions

; TODO for comprehension on the reverse?
(defn chop-until [pred s]
  (let [last-s (str (nth (reverse s) 0))] ; TODO perf, idiomatic?
    (if (or (pred last-s) (empty? last-s))
      s
      (recur pred (sutil/chop s))))) ; maybe should use loop, pred is useless here

(defn- contains-str? [search-s s]
  (> (.indexOf s search-s) -1))


;; Working with Paths
; A path consists of separators and components
; /component/component
; Current directory is .
; One dir above is ..

; The rules are not always clear, it's basically a big collection of edge cases.
; To understand this code, you have to check out the test cases in t-nsstring.

(defn- clean-component [#^String component]
  (.replaceAll (.replaceAll component "/./" "/") "/" ""))

; Base abstraction to generate paths
; TODO memoize
; More functional version see https://gist.github.com/1156682
(defn pathWithComponents
  "Returns a string built from the strings in a given collection by concatenating them with a path separator between each pair."
  [components]
    ; This is rather an imperative approach, but it works for all cases
    (when nil? components)
      ""
    (let [components-no-slash (remove #(= % "") (map #(clean-component %1) components)) ; Prepare components by removing any slash
          components-2 (if (= (first components) "/") (vec (cons "" components-no-slash)) components-no-slash) ; Add a leading slash if the orignal had one
          components-3 (if (= (last components) "") (vec (conj components-2 "/")) components-2)]  ; empty str becomes trailing /
      (if (= [""] components-3) "/"
        (reduce #(str %1 "/" %2) components-3)))) ; or sutil/join, or interpose

; Base abstraction to analyze paths
; TODO memoize
(defn pathComponents 
  "Returns a vector of strings containing, in order, each path component of the receiver."
  [nsstring]
    (if (empty? nsstring)
      []
      (let [components (remove empty? (seq (.split nsstring "/")))]
        (if (empty? components) ["/"]
            (let 
              [components-start (if (.startsWith nsstring "/") (cons "/" components) components)
               components-start-end (if (.endsWith nsstring "/") (conj (vec components-start) "/") (vec components-start))]
              components-start-end)))))

(defn isAbsolutePath? [nsstring] (.startsWith nsstring "/"))

(defn lastPathComponent "Returns the last path component of the receiver." [nsstring]
  (let [path-components (pathComponents nsstring)
        last-component (last path-components)]
    (if (and (> (count path-components) 1) (= last-component "/"))
      (nth path-components (dec (dec (count path-components))))
      last-component)))

(defn pathExtension [nsstring] 
  "The receiver's extension, if any (not including the extension divider)."
  (let [dot-index (.lastIndexOf nsstring ".")]
    (if 
      (= dot-index -1) ""
      (subs nsstring (inc dot-index)))))

(defn stringByAppendingPathComponent
  "Returns a new string made by appending to the receiver a given string."
  [nsstring component]
    (pathWithComponents (conj (pathComponents nsstring) component)))
  
(defn stringByAppendingPathExtension
  "Returns a new string made by appending to the receiver an extension separator followed by a given extension."
  [nsstring {:keys [ext]}]
  (cond
     (.startsWith nsstring "~") nsstring
     (.endsWith nsstring "/") (str (sutil/chop nsstring) "." ext)
     :else (str nsstring "." ext)))

(defn stringByDeletingLastPathComponent
  "Returns a new string made by deleting the last path component from the receiver, along with any final path separator."
  [nsstring]
    (let [path-components (pathComponents nsstring)]
      (cond 
        (empty? path-components) ""
        (= path-components ["/"]) "/"
        (= (count path-components) 1) ""
        :else
          (let [last-slash-removed (if (= (last path-components) "/") (butlast path-components) path-components)]
            (pathWithComponents (butlast last-slash-removed))))))

(defn stringByDeletingPathExtension
  "Returns a new string made by deleting the extension (if any, and only the last) from the receiver."
  [nsstring]
    (cond
      (= nsstring "/") "/"
      (and (.endsWith nsstring "/") (not (.contains nsstring "."))) (sutil/butlast 1 nsstring)
      (not (.contains nsstring ".")) nsstring
      :else
        (let [extension-location (.lastIndexOf nsstring ".")
              extension (subs nsstring extension-location)]
          (cond
            (.endsWith nsstring (str "/" extension)) nsstring
            (= nsstring extension) extension
            :else (subs nsstring 0 extension-location)))))

(defn stringsByAppendingPaths 
  "Returns an array of strings made by separately appending to the receiver each string in in a given array."
  [nsstring {:keys [paths]}]
    (map #(stringByAppendingPathComponent nsstring %1) paths))


; MAYBE

; System/getProperty "user.name" ;; but you can change this via Duser.name
; user.home

;(defn stringByAbbreviatingWithTildeInPath [nsstring]
;  (sutil/replace-str "~" ; TODO change to replace-first-str which is currently buggy
;   (System/getProperty "user.dir") ; Nope that's not the user's dir, it's the working dir
;   nsstring))
;(defn stringByExpandingTildeInPath [nsstring]
;  (sutil/replace-str "~" ; TODO change to replace-first-str which is currently buggy
;   (System/getProperty "user.dir") ; Nope that's not the user's dir, it's the working dir
;   nsstring))

; WONTIMPL

;(defn completePathIntoString [nsstring {:keys [outputName caseSensitive? matchesIntoArray filterTypes]}] 0)
;(defn fileSystemRepresentation [nsstring] "TODO")
;(defn getFileSystemRepresentation [nsstring {:keys [maxLength]}] "TODO")
;(defn stringByResolvingSymlinksInPath [nsstring] "TODO")
;(defn stringByStandardizingPath [nsstring] "TODO")
