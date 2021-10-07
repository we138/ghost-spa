(ns pagination)

(def offset 5)
(def dots ["..."])

(defn pages [size current per-page]
  (let [pages (vec (range 1 (+ 1 (/ size per-page))))
        delta 2
        left (- current delta)
        right (+ current delta 1)]
    (prn "size:" size "pages:" pages "current:" current "per-page:" per-page)
    (cond
      (<= (last pages) (+ 2 offset)) (vec (range (first pages) (last pages)))
      (<= current offset) (concat (range (first pages) right) dots (take-last 1 pages))
      (>= (- (last pages) offset) current) (concat (take 1 pages) dots (range left right) dots (take-last 1 pages))
      :else (concat (take 1 pages) dots (range left (+ 1 (last pages)))))))
