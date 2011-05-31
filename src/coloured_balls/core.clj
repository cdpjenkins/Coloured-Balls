(ns coloured-balls.core
  (:use [rosado.processing]
        [rosado.processing.applet]
        [ coloured-balls.motion])
  (:gen-class))

;; here's a function which will be called by Processing's (PApplet)
;; draw method every frame. Place your code here. If you eval it
;; interactively, you can redefine it while the applet is running and
;; see effects immediately

(defn draw-ball [ball]
	(fill (:red ball) (:green ball) (:blue ball))
	(ellipse (:x ball) (:y ball) (:radius ball) (:radius ball)))

(defn make-ball []
  {:x (rand-int 1000)
   :y (rand-int 1000)
   :red (rand-int 256)
   :blue (rand-int 256)
   :green (rand-int 256)
   :radius (+ 1 (rand-int 70))
   :heading (rand 360)
   :velocity (inc (rand 10))
   :shrink (rand-int 2)})

(def list-of-items (atom (take 200 (repeatedly make-ball))))

(defn reset-items [] (reset! list-of-items (take 200 (repeatedly make-ball))))

(defn draw-balls [balls]
  (do
    (background 0 0 0)
    (doseq [ball balls]
      (draw-ball ball))))

(defn move-balls! [coll]
  (swap! coll (fn [l] (doall (map #(move % (remove (fn [item] (= item)) l)) l)))))


(defn draw
  "Example usage of with-translation and with-rotation."  []
  (draw-balls @list-of-items)
  (move-balls! list-of-items))
(defn setup []
  "Runs once."
  (smooth)
  (no-stroke)
  (fill 226)
  (framerate 10))

;; Now we just need to define an applet:

(defapplet balls :title "Coloured balls"
  :setup setup :draw draw :size [1000 1000])

(defn -main [& args]
 (run balls true))

