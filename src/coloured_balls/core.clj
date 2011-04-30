(ns coloured-balls.core
  (:use [rosado.processing]
        [rosado.processing.applet])
  (:gen-class))

(def WIDTH 400)
(def HEIGHT 400)

;; here's a function which will be called by Processing's (PApplet)
;; draw method every frame. Place your code here. If you eval it
;; interactively, you can redefine it while the applet is running and
;; see effects immediately

(defn draw-ball [ball]
;  (stroke (255 255 255))
  (println "hoss" ball)
  (fill (:red ball) (:green ball) (:blue ball))
  (ellipse (:x ball) (:y ball) (:radius ball) (:radius ball)))

(defn make-ball []
  {:x (rand-int 400)
   :y (rand-int 400)
   :vx (- (rand-int 10) 5)
   :vy (- (rand-int 10) 5)
   :red (rand-int 256)
   :blue (rand-int 256)
   :green (rand-int 256)
   :radius (+ 1 (rand-int 70))})

(def *balls*
  (atom 
   (for [_ (range 10)]
     (make-ball))))

(defn move-balls [balls]
  ;; Massive note: if we implement gravity then got to make sure
  ;; kinetic+potential energy is preserved. Same if we implement
  ;; bouncing off other balls (conservation of both momentum and
  ;; energy) which I really can't remember how to do.
  (for [ball balls]
    (let [nx (+ (:x ball) (:vx ball))
          ny (+ (:y ball) (:vy ball))
          nvx (if (or (< nx 0) (> nx WIDTH))
                (- (:vx ball))
                (:vx ball))
          nvy (if (or (< ny 0) (> ny HEIGHT))
                (- (:vy ball))
                (:vy ball))]
                
          
      (assoc ball :x nx
                  :y ny
                  :vx nvx
                  :vy nvy))))

(defn draw
  "Example usage of with-translation and with-rotation."
  []
  (swap! *balls* move-balls)
  (doseq [ball @*balls*]
    (draw-ball ball))
  ;;	(draw-ball (make-ball))
  )

(defn setup []
  "Runs once."
  (smooth)
;  (no-stroke)
  (fill 226)
  (framerate 10))

;; Now we just need to define an applet:

(defapplet balls-applet :title "Coloured balls"
  :setup setup :draw draw :size [WIDTH HEIGHT])

(defn go []
  )


; (run balls-applet true)


(defn -main []
  (println "Main!"))
