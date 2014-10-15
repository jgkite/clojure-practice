(ns practice.regex-utils
  (:require [clojure.string :as str]))

(defn regex-escape
  "Escape a string for use as a regular expression, where the regex will match on
   the literal characters."
  [string]
  (let [special-characters [\\ \^ \$ \. \| \? \* \+ \( \) \[ \{]
        regex-escape-map (into {} (map #(hash-map % (str "\\" %)) special-characters))]
    (str/escape string regex-escape-map)))

(defn build-regex
  "Given a list of strings and optional regular expression, outputs a single regular
   expression suitable for use as a matcher."
  [strings & [regex]]
  (->> strings
       (map regex-escape)
       (cons regex)
       (filter identity)
       (map #(str "(" % ")"))
       (str/join "|")
       re-pattern))
