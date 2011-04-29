(ns coloured-balls.core
  (:use [rosado.processing]
        [rosado.processing.applet])
  (:gen-class))

;; here's a function which will be called by Processing's (PApplet)
;; draw method every frame. Place your code here. If you eval it
;; interactively, you can redefine it while the applet is running and
;; see effects immediately

(defn draw-ball [ball]
  (fill (:red ball) (:green ball) (:blue ball))
  (ellipse (:x ball) (:y ball) (:radius ball) (:radius ball)))

(defn make-ball []
  {:x (rand-int 400) :y (rand-int 400) :red (rand-int 256) :blue (rand-int 256) :green (rand-int 256) :radius (+ 1 (rand-int 70))})

(def *balls*
  (atom 
   (for [_ (range 10)]
     (make-ball))))

(defn draw
  "Example usage of with-translation and with-rotation."
  []
  (doseq [ball @*balls*]
    (draw-ball ball))
  ;;	(draw-ball (make-ball))
  )

(defn setup []
  "Runs once."
  (smooth)
  (no-stroke)
  (fill 226)
  (framerate 10))

;; Now we just need to define an applet:

(defapplet balls-applet :title "Coloured balls"
  :setup setup :draw draw :size [400 400])

(defn go []
  )


; (run balls-applet true)


(defn -main []
  (println "Main!"))
