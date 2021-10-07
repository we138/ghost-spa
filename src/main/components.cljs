(ns components
  (:require [pagination :as pn]))

(defn input [value control-name placeholder disabled on-change]
  [:div.w-full.flex.space-x-4.md:items-center
   [:div {:class "flex w-1/5"}
    [:label.text-gray-500.font-mono control-name]]
   [:div {:class "flex w-4/5"}
    [:input.w-full.appearance-none.border.border-transparent.bg-white.text-gray-700.placeholder-gray-400.shadow-md.rounded-lg.text-base.focus:outline-none.focus:ring-2.focus:ring-purple-600.focus:border-transparent.text-pink-400
     {:type        "text"
      :disabled    disabled
      :placeholder placeholder
      :value       value
      :on-change   on-change}]]])

(defn button [inputs-empty? click]
  [:div
   [:button    
    {:disabled inputs-empty?
     :class (str "flex-shrink-0  text-white text-base py-2 px-4 rounded-lg shadow-md focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 focus:ring-offset-purple-200 font-mono" " " (if inputs-empty? "bg-purple-100" "bg-purple-600 hover:bg-purple-700"))
     :on-click click} "Let's go"]])

(defn index [items]
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

(defn pagination [total current-page per-page on-click]
  [:div.flex.flex-col.items-center.my-12
   [:div.flex.h-12.font-medium.rounded-full.bg-purple-600
    (for [page (pn/pages total current-page per-page)]
      ^{:key page}
      [:div
       {:class
        (str "w-12 md:flex justify-center items-center hidden cursor-pointer leading-5 transition duration-150 ease-in rounded-full text-white"
             " "
             (if (= page current-page) "bg-pink-400" "bg-purple-600"))
        :on-click (on-click page)} page])]])

(defn error [errors]
  [:div.flex.flex-col.space-y-2.bg-red-100.border.border-red-600.text-red-700.px-4.py-3.rounded.relative
   "Oops! Something went wrong:"
   [:ul.list-decimal.px-8
    (for [[key messages] errors]
      [:li (str (name key) ":")
       [:ul.list-disc.px-2
        (for [message messages]
          [:li message])]])]])

(defn loader []
  [:div.flex.justify-center.items-center
   [:div.animate-spin.rounded-full.h-32.w-32.border-t-2.border-b-2.border-purple-600]])

