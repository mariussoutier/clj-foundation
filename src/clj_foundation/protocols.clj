(ns clj-foundation.protocols)

;; Just for completeness' sake

;(defprotocol NSObject
;  (class [a-class]))

(defprotocol NSCoding
  "The NSCoding protocol declares the two methods that a class must implement 
   so that instances of that class can be encoded and decoded."
  (initWithCoder [decoder])
  (encodeWithCoder [encoder]))
