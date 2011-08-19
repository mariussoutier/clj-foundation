(ns clj-foundation.perf-nsstring
  (:use clj-foundation.nsstring))

;; Perfomance tests

(def *repetitions* 1000)

(time 
  (dotimes [x *repetitions*]
    (pathWithComponents ["Apple" "Local" "Library" "Frameworks"])))

(deftest ^{:tags [:performance]} perf-pathWithComponents 
  (dotimes [x *repetitions*]
    (pathWithComponents ["Apple" "Local" "Library" "Frameworks"])))
