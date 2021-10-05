(ns api
  (:require [ajax.core :refer [ajax-request json-request-format json-response-format]]))

(goog-define URL "http://localhost:3013/api")

;; (def uri (str "") "http://localhost:3013/api/search")

(defn error-handler [response] (println "Error:" response))

(defn search [params handler]
  (ajax-request
   {:uri (str URL "/search")
    :method :post
    :params params
    :handler handler
    :error-handler error-handler
    :format (json-request-format)
    :response-format (json-response-format {:keywords? true})}))
