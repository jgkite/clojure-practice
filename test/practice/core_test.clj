(ns practice.core-test
  (:use expectations
        practice.core))

;; An empty string will return 0.
(expect 0 (sum-strings ""))

;; A string with a single number will return that number as an integer.
(expect 1 (sum-strings "1"))
(expect 10 (sum-strings "10"))

;; A string containing two or more comma-separated numbers will add them
;; and return the result.
(expect 10 (sum-strings "5,5"))
(expect 12 (sum-strings "0,12"))
(expect 22 (sum-strings "0,10,12"))
(expect 10 (sum-strings "1,2,3,4"))

;; When multiple strings are passed, they are added together.
(expect 5 (sum-strings "1,4"))
(expect 10 (sum-strings "1,4" "5"))
(expect 100 (sum-strings "1,4" "5", "10,20,60"))

;; Newlines are allowed.
;; e.g. "1\n2,3" should return 6.
(expect 6 (sum-strings "1\n2,3"))
(expect 15 (sum-strings "10
                         2,3"))

;; Alternative delimiters can be declared. To change the delimiter,
;; the beginning of the string will contain a separate line that looks
;; like this: "//[delimiter]\n[numbers]"
(expect 3 (sum-strings "//;\n1;2"))
(expect 21 (sum-strings "//-
                        1-20"))

;; Negative numbers throw an exception.
(expect "Negatives not allowed: -1"
        (try
          (sum-strings "-1")
          (catch Exception e (.getMessage e))))
(expect "Negatives not allowed: -10,-2"
        (try
          (sum-strings "-10,-2")
          (catch Exception e (.getMessage e))))
(expect "Negatives not allowed: -4,-5"
        (try
          (sum-strings "2,-4,3,-5")
          (catch Exception e (.getMessage e))))

;; Numbers greater than 1,000 are ignored.
(expect 2 (sum-strings "1001,2"))
(expect 1000 (sum-strings "1000"))

;; Delimiters can be any length.
(expect 6 (sum-strings "//***\n1***2***3"))

;; Allow multiple delimiters with a bracket syntax.
(expect 6 (sum-strings "//[*][%]\n1*2%3"))

;; Allow multiple delimiters of any length.
(expect 10 (sum-strings "//[^^^][!]\n5^^^2!3"))
