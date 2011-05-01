(ns coloured-balls.core
  (:use [rosado.processing]
        [rosado.processing.applet])
  (:gen-class))

(def WIDTH 400)
(def HEIGHT 400)

(def GRAVITY (/ 1.0 1))

(defn my-rand [min max]
  (let [range (- max min)]
    (+ (rand range) min)))

(defn calculate-mass [ball]
  (let [r (:radius ball)]
    (* r r)))

(defn calculate-kinetic-engery [ball]
  (let [m (calculate-mass ball)
        vx (:vx ball)
        vy (:vy ball)
        v2 (+  (* vx vx) (* vy vy) )]
    (* 1/2 m v2)))

(defn calculate-potential-energy [ball]
  (let [h (- HEIGHT ( :y ball))]
    (* (calculate-mass ball) GRAVITY h)))

(defn calculate-total-energy [ball]
  (+ (calculate-potential-energy ball) (calculate-kinetic-engery ball)))

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

(defn distance [x1 y1 x2 y2]
  (let [dx (- x2 x1)
        dy (- y1 y2)]
    (sqrt (+ (* dx dx) (* dy dy)))))

(defn balls-overlap? [ball1 ball2]
  (< (distance (:x ball1)
               (:y ball1)
               (:x ball2)
               (:y ball2))
     (+ (:radius ball1) (:radius ball2))))


(defn add-ball [balls]
  (let [ball (make-ball)]
    (if (some #(balls-overlap? ball %) balls)
      (add-ball balls)
      (cons (make-ball) balls))))


(def *balls*
  (atom 
   (reduce  (fn [balls _] (add-ball balls))
            ()
            (range 1))))
;   (for [_ (range 20)]
;     (make-ball))))

(defn move-balls [balls]
  ;; Massive note: if we implement gravity then got to make sure
  ;; kinetic+potential energy is preserved. Same if we implement
  ;; bouncing off other balls (conservation of both momentum and
  ;; energy) which I really can't remember how to do.
  (for [ball balls]
    (let [vx (:vx ball)
          vy (:vy ball)

          ;; gravity
          vy (+ vy GRAVITY)

          x (+ (:x ball) vx)
          y (+ (:y ball) vy)

          radius (:radius ball)

          ;; apply bounce-off-walls
          vx (if (or (< (- x radius) 0) (> (+ x radius) WIDTH))
               (- vx)
               vx)

          vy (if (or (< (- y radius) 0) (> (+ y radius) HEIGHT))
               (- vy)
               vy)


          ]
      (println ( calculate-total-energy ball))
          
      (assoc ball :x x
                  :y y
                  :vx vx
                  :vy vy))))

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
