(ns clj-foundation.t-nsstring
  (:use clj-foundation.nsstring)
  (:use midje.sweet))

(println "Proving facts...")

(fact (pathWithComponents ["Apple" "Local" "Library" "Frameworks"]) => "Apple/Local/Library/Frameworks")
(fact (pathWithComponents ["/" "Apple" "Local" "Library" "Frameworks"]) => "/Apple/Local/Library/Frameworks")
(fact (pathWithComponents ["/" "Apple" "Local" "Library" "Frameworks/" "/"]) => "/Apple/Local/Library/Frameworks")
(fact (pathWithComponents ["/" "Apple" "Local" "///////"  "Library" "Frameworks" "/"]) => "/Apple/Local/Library/Frameworks")
(fact (pathWithComponents ["/"]) => "/")
(fact (pathWithComponents ["/" "/"]) => "/")
; This is in the Apple docs but doesn't really work that way:
;(fact (pathWithComponents ["/" "Apple" "Local" "Library" "Frameworks" ""]) => "/Apple/Local/Library/Frameworks/")

(fact (pathComponents "tmp/scratch") => ["tmp" "scratch"])
(fact (pathComponents "/tmp/scratch") => ["/" "tmp" "scratch"])
(fact (pathComponents "/tmp/scratch/") => ["/" "tmp" "scratch" "/"]) ; this is not consistent
(fact (pathComponents "/tmp/./scratch/untitled.tiff") => ["/" "tmp" "." "scratch", "untitled.tiff"])
(fact (pathComponents "/tmp/scratch////untitled.tiff") => ["/" "tmp" "scratch", "untitled.tiff"])
(fact (pathComponents "/") => ["/"])
(fact (pathComponents "") => [])

(fact (isAbsolutePath? "Apple/Local/Library/Frameworks") => false)
(fact (isAbsolutePath? "/Apple/Local/Library/Frameworks") => true)

(fact (lastPathComponent "/tmp/scratch.tiff") => "scratch.tiff")
(fact (lastPathComponent "/tmp/scratch") => "scratch")
(fact (lastPathComponent "/tmp/") => "tmp")
(fact (lastPathComponent "scratch") => "scratch")
(fact (lastPathComponent "/") => "/")

(fact (pathExtension "/tmp/scratch.tiff") => "tiff")
(fact (pathExtension "/tmp/scratch") => "")
(fact (pathExtension "/tmp/") => "")
(fact (pathExtension "/tmp/scratch..tiff") => "tiff")

(fact (stringByAppendingPathComponent "/tmp" "scratch.tiff") => "/tmp/scratch.tiff")
(fact (stringByAppendingPathComponent "/tmp/" "scratch.tiff") => "/tmp/scratch.tiff")
(fact (stringByAppendingPathComponent "/" "scratch.tiff") => "/scratch.tiff")
(fact (stringByAppendingPathComponent "" "scratch.tiff") => "scratch.tiff")

(fact (stringByAppendingPathExtension "/tmp/scratch.old" {:ext "tiff"}) => "/tmp/scratch.old.tiff")
(fact (stringByAppendingPathExtension "/tmp/scratch." {:ext "tiff"}) => "/tmp/scratch..tiff")
(fact (stringByAppendingPathExtension "/tmp/" {:ext "tiff"}) => "/tmp.tiff")
(fact (stringByAppendingPathExtension "scratch" {:ext "tiff"}) => "scratch.tiff")
(fact (stringByAppendingPathExtension "~/scratch" {:ext "tiff"}) => "~/scratch")

(fact (stringByDeletingLastPathComponent "/tmp/lock/scratch.tiff") => "/tmp/lock")
(fact (stringByDeletingLastPathComponent "/tmp/lock") => "/tmp")
(fact (stringByDeletingLastPathComponent "/tmp/lock/") => "/tmp")
(fact (stringByDeletingLastPathComponent "/tmp/") => "/")
(fact (stringByDeletingLastPathComponent "/tmp") => "/")
(fact (stringByDeletingLastPathComponent "/") => "/")
(fact (stringByDeletingLastPathComponent "scratch.tiff") => "")
(fact (stringByDeletingLastPathComponent "//////tmp//////") => "/")

(fact (stringByDeletingPathExtension "/tmp/scratch.tiff") => "/tmp/scratch")
(fact (stringByDeletingPathExtension "/tmp/scratch.tiff/") => "/tmp/scratch")
(fact (stringByDeletingPathExtension "/tmp/") => "/tmp")
(fact (stringByDeletingPathExtension "scratch.bundle/") => "scratch")
(fact (stringByDeletingPathExtension "scratch..tiff") => "scratch.")
(fact (stringByDeletingPathExtension ".tiff") => ".tiff")
(fact (stringByDeletingPathExtension "..tiff") => ".")
(fact (stringByDeletingPathExtension "/") => "/")
(fact (stringByDeletingPathExtension "untitled") => "untitled")
(fact (stringByDeletingPathExtension "/untitled/.tiff") => "/untitled/.tiff")

(fact (stringsByAppendingPaths "/" {:paths ["untitled" "/var" "untitled.tiff"]}) => ["/untitled" "/var" "/untitled.tiff"])
(fact (stringsByAppendingPaths "/private" {:paths ["untitled" "/var" "untitled.tiff"]}) => ["/private/untitled" "/private/var" "/private/untitled.tiff"])
(fact (stringsByAppendingPaths "/private/" {:paths ["untitled" "/var" "untitled.tiff"]}) => ["/private/untitled" "/private/var" "/private/untitled.tiff"])
(fact (stringsByAppendingPaths "/private//" {:paths ["untitled" "/var" "untitled.tiff"]}) => ["/private/untitled" "/private/var" "/private/untitled.tiff"])
