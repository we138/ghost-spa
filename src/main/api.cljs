(ns api
  (:require [ajax.core :refer [ajax-request json-request-format json-response-format]]))

(def uri "http://localhost:3013/api/search")

(defn error-handler [response] (println "Error:" response))

(defn search [params handler]
  (ajax-request
   {:uri uri
    :method :post
    :params params
    :handler handler
    :error-handler error-handler
    :format (json-request-format)
    :response-format (json-response-format {:keywords? true})}))
