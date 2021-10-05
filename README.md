
# Table of Contents

1.  [Ghost SPA](#orgffc5274)
2.  [Demo](#org4992c34)
3.  [How it works?](#org7e3b03d)
4.  [Installation](#orgd3c3e1c)
    1.  [requirements](#org0a1edee)
    2.  [running](#org8493dcd)
5.  [Release](#org6c5ff8e)


<a id="orgffc5274"></a>

# Ghost SPA

Single page application for [Ghost-API](https://github.com/we138/ghost-api), based on clojurescript, reagent, shadow-cljs and tailwindcss.


<a id="org4992c34"></a>

# Demo

<https://we138.github.io/ghost-spa/>


<a id="org7e3b03d"></a>

# How it works?

reagent for react-like components, tailwindcss for styling and shadow-cljs for compile clojurescript code.


<a id="orgd3c3e1c"></a>

# Installation


<a id="org0a1edee"></a>

## requirements

you will need:

-   any java SDK (version 8 or higher)
-   clojure
-   node


<a id="org8493dcd"></a>

## running

npm install &#x2013;save-dev shadow-cljs
npx shadow-cljs watch :application


<a id="org6c5ff8e"></a>

# Release

npx shadow-cljs release :app

