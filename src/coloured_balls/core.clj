(ns coloured-balls.core
  (:use [rosado.processing]
        [rosado.processing.applet])
  (:gen-class))

(def WIDTH 400)
(def HEIGHT 400)

(defn my-rand [min max]
  (let [range (- max min)]
    (+ (rand range) min)))

;; here's a function which will be called by Processing's (PApplet)
;; draw method every frame. Place your code here. If you eval it
;; interactively, you can redefine it while the applet is running and
;; see effects immediately

(defn draw-ball [ball]
  (stroke 0 0 0)
  (fill (:red ball) (:green ball) (:blue ball))
  (let [x (:x ball)
        y (:y ball)
        diameter (* (:radius ball) 2)]
    (ellipse x y diameter diameter)    
    )
  )

(defn make-ball []
  (let [radius (rand 50)]
    {:x (my-rand radius (- WIDTH radius))
     :y (my-rand radius (- HEIGHT radius))
     :vx (- (rand 10) 5)
     :vy (- (rand 10) 5)
     :red (rand-int 256)
     :blue (rand-int 256)
     :green (rand-int 256)
     :radius radius}))

(def *balls*
  (atom 
   (for [_ (range 20)]
     (make-ball))))

(defn move-balls [balls]
  ;; Massive note: if we implement gravity then got to make sure
  ;; kinetic+potential energy is preserved. Same if we implement
  ;; bouncing off other balls (conservation of both momentum and
  ;; energy) which I really can't remember how to do.
  (for [ball balls]
    (let [nx (+ (:x ball) (:vx ball))
          ny (+ (:y ball) (:vy ball))
          radius (:radius ball)
          nvx (if (or (< (- nx radius) 0) (> (+ nx radius) WIDTH))
                (- (:vx ball))
                (:vx ball))
          nvy (if (or (< (- ny radius) 0) (> (+ ny radius) HEIGHT))
                (- (:vy ball))
                (:vy ball))]
                
          
      (assoc ball :x nx
                  :y ny
                  :vx nvx
                  :vy nvy))))

;;; 
(defn cls []
  (smooth)
  (no-stroke)
  (fill 226)
  (rect 0 0 WIDTH HEIGHT))

(defn draw
  "Example usage of with-translation and with-rotation."
  []
  (swap! *balls* move-balls)

  (cls)
  (doseq [ball @*balls*]
    (draw-ball ball))
  (stroke 255 0 0))



(defn setup []
  "Runs once."
  (cls)
  (framerate 10))

;; Now we just need to define an applet:

(defapplet balls-applet :title "Coloured balls"
  :setup setup :draw draw :size [WIDTH HEIGHT])

(defn go []
  )


; (run balls-applet true)


(defn -main []
  (println "Main!"))
