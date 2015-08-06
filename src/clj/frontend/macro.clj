(ns frontend.macro)

(defmacro foobar [x y]
  `(js/console.log (str {:foo ~x :bar ~y})))
