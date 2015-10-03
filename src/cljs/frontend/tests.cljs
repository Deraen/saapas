(ns frontend.tests
  (:require [cljs.test :refer-macros [deftest is] :as test]))

(enable-console-print!)

(deftest test-test
  (is (= 2 (+ 1 1)) "1+1 is 2"))

(test/run-all-tests #"^frontend.*-test$")
