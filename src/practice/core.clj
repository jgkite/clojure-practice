(ns practice.core
  (:require [clojure.string :as str])
  (:use practice.regex-utils))

(declare sum-string)

(def default-delimiter ",")

(defn sum-strings
  "Solving the String Calculator Kata, in Clojure."
  [& strings]
  (->> strings
      (map #(sum-string %))
      (reduce +)))

(defn- get-delimiters
  "Parses the delimiter from a string, where the delimiter is the
   character or characters after the // and before the newline.
   Also supports multiple delimiters of the form [del-1][del-2]."
  [string & [default]]
  (let [first-line (first (str/split-lines string))
        prefix-regex #"^/{2}(((\[(.+)\])+)|(.*))$"
        matches (filter identity (re-find prefix-regex first-line))]
    (if (empty? matches)
      (if default [default] nil)
      (-> matches
          (last)
          (str/split #"\]\[")))))

;; TODO: Have this just check for the // start characters instead?
(defn- has-delimiter?
  "Determines whether a string contains a delimiter prefix"
  [string]
  (not (empty? (get-delimiters string))))

(defn- sum-string
  "Takes a delimited string of numeric values, adds them
   together, and returns the result."
  [string]
  (let [delimiters (get-delimiters string default-delimiter)
        delimiter-regex (build-regex delimiters #"\s+")
        string-without-prefix (if (has-delimiter? string)
                                (str/trim (last (str/split string #"\n" 2)))
                                string)]
    (if (str/blank? string-without-prefix)
      0
      (as-> string-without-prefix $
            (str/split $ delimiter-regex)
            (map #(Integer. %) $)
            (filter #(<= % 1000) $)
            (let [negatives (filter #(< % 0) $)]
              (if (not (empty? negatives))
                (throw (Exception. (str "Negatives not allowed: " (str/join "," negatives))))
                $))
            (reduce + $)))))
