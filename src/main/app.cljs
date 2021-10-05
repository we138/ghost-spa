(ns app
  (:require
   [reagent.dom :as rdom]
   [reagent.core :as rcore]
   [api :as ghost-api]
   [pagination :as pn]))

(def db (rcore/atom {:page 1 :per_page 30}))

(defn switch-page [page]
  (swap! db assoc :page page :searching true)
  (ghost-api/search
   (merge (:search-query @db) (select-keys @db [:page :per_page]))
   (fn [[status body]]
     (swap! db assoc :searching false :search-status status :search-body body))))

(defn input-component [name control-name placeholder disabled]
  [:div.w-full.flex.space-x-4.md:items-center
   [:div {:class "flex w-1/5"}
    [:label.text-gray-500.font-mono control-name]]
   [:div {:class "flex w-4/5"}
    [:input.w-full.appearance-none.border.border-transparent.bg-white.text-gray-700.placeholder-gray-400.shadow-md.rounded-lg.text-base.focus:outline-none.focus:ring-2.focus:ring-purple-600.focus:border-transparent.text-pink-400
     {:type        "text"
      :disabled    disabled
      :placeholder placeholder
      :value       (get @db name)
      :on-change   #(swap! db assoc name (-> % .-target .-value))}]]])

(defn button [inputs-empty?]
  [:div
   [:button    
    {:disabled inputs-empty?
     :class (str "flex-shrink-0  text-white text-base py-2 px-4 rounded-lg shadow-md focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 focus:ring-offset-purple-200 font-mono" " " (if inputs-empty? "bg-purple-100" "bg-purple-600 hover:bg-purple-700"))
     :on-click
     (fn []
       (swap! db assoc :searching true)

       (ghost-api/search
        (merge {:page 1 :per_page 30} (select-keys @db [:name :language :query]))
        (fn [[status body]]
          (swap! db assoc :page 1 :per_page 30 :searching false :search-status status :search-body body :search-query (select-keys @db [:name :language :query])))))} "Let's go"]])

(defn pagination-component [total current-page per-page]
  [:div.flex.flex-col.items-center.my-12
   [:div.flex.h-12.font-medium.rounded-full.bg-purple-600
    (for [page (pn/pages total current-page per-page)]
      ^{:key page}
      [:div
       {:class
        (str "w-12 md:flex justify-center items-center hidden cursor-pointer leading-5 transition duration-150 ease-in rounded-full text-white"
             " "
             (if (= page current-page) "bg-pink-400" "bg-purple-600"))
        :on-click #(switch-page page)} page])]])

(defn repositories-list [total items]
  [:ul.flex.flex-col.space-y-4
   (for [item items]
     ^{:key item}
     [:li.flex.flex-col.space-y-2.border-b-2.border-opacity-50
      [:div
       [:a.no-underline.hover:underline.font-mono.text-pink-400.text-xl {:href (:html_url item)} (:name item)]]
      [:div
       [:a.font-mono.text-l (:description item)]]
      [:div.pb-3
       [:a.text-purple-600.text-xs (:language item)]]])])

(defn search []
  (let [search-status (:search-status @db)
        search-result (:search-body @db)
        current-page (:page @db)
        query-input-blank? (clojure.string/blank? (@db :query))
        name-input-blank? (clojure.string/blank? (@db :name))
        language-input-blank? (clojure.string/blank? (@db :language))]

    [:div.p-6.m-auto.mx-auto.bg-white.rounded-xl.shadow-md.flex.flex-col.items-center.space-y-4.max-w-2xl
     [:p.font-mono.text-3xl.text-pink-400 "Search for a Github repo"]

     [input-component :query "Query" "jquery+in:name+language:ruby"
      (not (and name-input-blank? language-input-blank?))]

     [input-component :name "Name" "rom-rb, hanami, karafka" (not query-input-blank?)]
     [input-component :language "Language" "ruby, clojurescript, kotlin" (not query-input-blank?)]

     [button (and language-input-blank? name-input-blank? query-input-blank?)]

     (when (and (= false (:searching @db)) (= false search-status))
       [:div.flex.flex-col.space-y-2.bg-red-100.border.border-red-600.text-red-700.px-4.py-3.rounded.relative
        "Oops! Something went wrong:"
        [:ul.list-decimal.px-8
         (for [[key messages] (get-in @db [:search-body :response])]
           [:li (str (name key) ":")
            [:ul.list-disc.px-2
             (for [message messages]
               [:li message])]])]])

     (if (:searching @db)
       [:div.flex.justify-center.items-center
        [:div.animate-spin.rounded-full.h-32.w-32.border-t-2.border-b-2.border-purple-600]])

     (when (and (= false (:searching @db)) (= true search-status))
       [:div.flex.flex-col.space-y-6
        [:div.border-b-2.border-opacity-50.pb-3
         [:p.font-mono.text-3xl.text-pink-400 "Results: " (get-in search-result [:meta :total])]]
        [repositories-list (get-in search-result [:meta :total]) (:data search-result)]
        [pagination-component (get-in search-result [:meta :total]) current-page (get-in search-result [:meta :per_page])]])]))

(defn init []
  (rdom/render
   [:div.flex.min-h-screen.bg-gradient-to-r.from-purple-50.to-pink-50
    [search]]
   (.-body js/document)))
