(ns app
  (:require
   [reagent.dom :as rdom]
   [reagent.core :as rcore]
   [api :as ghost-api]
   [components :as c]
   [clojure.string :as cstr]))

(def db (rcore/atom {:page 1 :per_page 30}))

(defn switch-page [page]
  (swap! db assoc :page page :searching true)
  (ghost-api/search
   (merge (:search-query @db) (select-keys @db [:page :per_page]))
   (fn [[status body]]
     (swap! db assoc :searching false :search-status status :search-body body))))

(defn click-button []
  (let [keys (select-keys @db [:name :language :query])
        payload (merge keys {:page 1 :per_page 30})]
    (swap! db assoc :searching true)
    (ghost-api/search
     payload
     (fn [[status body]]
       (swap! db assoc :page 1 :per_page 30 :searching false :search-status status :search-body body :search-query keys)))))

(defn search []
  (let [searching (:searching @db)
        search-status (:search-status @db)
        search-result (:search-body @db)
        current-page (:page @db)
        total (get-in search-result [:meta :total])
        per-page (get-in search-result [:meta :per_page])
        query-input-blank? (cstr/blank? (@db :query))
        name-input-blank? (cstr/blank? (@db :name))
        language-input-blank? (cstr/blank? (@db :language))]

    [:div.p-6.m-auto.mx-auto.bg-white.rounded-xl.shadow-md.flex.flex-col.items-center.space-y-4.max-w-2xl
     [:p.font-mono.text-3xl.text-pink-400 "Search for a Github repo"]

     [c/input (:query @db) "Query" "jquery+in:name+language:ruby"
      (not (and name-input-blank? language-input-blank?))
      #(swap! db assoc :query (-> % .-target .-value))]

     [c/input (:name @db) "Name" "rom-rb, hanami, karafka"
      (not query-input-blank?)
      #(swap! db assoc :name (-> % .-target .-value))]

     [c/input (:language @db) "Language" "ruby, clojurescript, kotlin"
      (not query-input-blank?)
      #(swap! db assoc :language (-> % .-target .-value))]

     [c/button
      (and language-input-blank? name-input-blank? query-input-blank?) #(click-button)]

     (when (and (false? searching) (false? search-status))
       [c/error (:response search-result)])

     (when searching
       [c/loader])

     (when (and (false? searching) (true? search-status))
       [:div.flex.flex-col.space-y-6
        [:div.border-b-2.border-opacity-50.pb-3
         [:p.font-mono.text-3xl.text-pink-400 "Results: " total]]

        [c/index (:data search-result)]

        [c/pagination
         total current-page per-page
         (fn [page] #(switch-page page))]])]))

(defn init []
  (rdom/render
   [:div.flex.min-h-screen.bg-gradient-to-r.from-purple-50.to-pink-50
    [search]]
   (.-body js/document)))
