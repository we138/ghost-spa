(ns pagination)

(def pagination-offset 5)

(defn pages [size current-page per-page]
  (let [total (vec (range 1 (+ 1 (/ size per-page))))]
    (if (> (count total) pagination-offset)
      (if (= current-page pagination-offset)
        (concat (take (+ pagination-offset 2) total) ["..."] (take-last 1 total))
        (if (< current-page pagination-offset)
          (concat (take pagination-offset total) ["..."] (take-last 1 total))
          (concat (take 1 total) ["..."] (subvec total (- current-page 2) (+ current-page 2)) ["..."] (take-last 1 total))))
      total)))
