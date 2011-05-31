(ns coloured-balls.motion
	(:use [clojure.contrib.generic.math-functions :only (sqrt, round, cos, sin)]))
	
(defn degrees-to-radians [degrees]
	(* (/ Math/PI 180) degrees))

(defn collided-with? [item1 item2]
  (let [dx (- (:x item1) (:x item2))
        dy (- (:y item1) (:y item2))
              
        distance2 (+ (* dx dx) (* dy dy))
        distance (sqrt distance2)
        radius1 (:radius item1)
        radius2 (:radius item2)]
    (> (+ radius1 radius2)
       distance)))

(defn collided-with-any? [item items]
  (some #(collided-with? item %) items))


(defn move [item items]
	(let [heading (degrees-to-radians (:heading item))
              velocity (:velocity item)
              old-x (:x item)
              old-y (:y item)
              new-x  (* velocity (cos heading))
              new-y  (* velocity (sin heading))
              new-radius (+ (:radius item)
                            (if (collided-with-any? item items)
                              1
                              -1))
              ]
		(conj {:x (+ old-x new-x) :y (+ old-y new-y) :radius new-radius} (dissoc item :x :y :radius))))


; Destruction, Eating, Hunting, Exploding

