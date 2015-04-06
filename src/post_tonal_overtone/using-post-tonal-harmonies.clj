(ns post-tonal-overtone.core
  (:use overtone.live
        overtone.inst.sampled-piano
	clojure.repl))

(defn play-piano-chord [a-chord]
        (doseq [note a-chord] (sampled-piano note)))

(play-piano-chord [60 64 67])

(defn play-quieter-piano-chord [a-chord]
  (let [level (rand-nth [0.05 0.1 0.2])]
    (doseq [note a-chord] (sampled-piano note :level level))))

(play-quieter-piano-chord [60 64 67])

(load "set-class-data")

(rand-nth tetrachords)

(defn voice-rand-set [set-type]
  (let [set (rand-nth set-type)
        voiced-set (map #(+ (rand-nth [36 48 60 72]) %) set)
        set-voicing-pair (list set voiced-set)]
    (do
      (println set-voicing-pair)
      set-voicing-pair)))

(voice-rand-set tetrachords)

(def zero12s (subvec (vec tetrachords) 0 6))

(def dia1 (subvec (vec tetrachords) 7 11))

(def tri-heavy (subvec (vec tetrachords) 11 17))

(def whole-tones (subvec (vec tetrachords) 17  25))

(defn tetra-probs1 [[z x y w] dist]
  (let [n (rand)]
    (cond
      (>= n z) (rand-nth whole-tones)
      (>= n x) (rand-nth tri-heavy)
      (>= n y) (rand-nth dia1)
      (>= n w) (rand-nth zero12s)
      :else (rand-nth hexachords ))))

(tetra-probs1 0.8 0.5 0.2 0.1)

(defn tetra-probs2 [z x y w]
  (let [n (rand)]
    (cond
      (>= n z) (rand-nth whole-tones)
      (>= n x) (rand-nth tri-heavy)
      (>= n y) (rand-nth dia1)
      (>= n w) (rand-nth zero12s)
      :else (rand-nth hexachords ))))

(tetra-probs2 0.8 0.5 0.2 0.1)

(defn voice-and-transpose-rand-set [set-type tn-level]
  (let [set (rand-nth set-type)
        voiced-set (map #(+ (rand-nth [36 48 60 72]) %) set)
	visible-pairings (map #(list % (mod % 12)) voiced-set)
        transposed-set (map #(+ tn-level %) voiced-set)
	paired-transposed-set (map #(list (mod % 12) (+ tn-level %)) (sort voiced-set))
        set-voicing-group (list :set set
                                :tn-level tn-level
				:reg visible-pairings
                                :sorted-trans paired-transposed-set)]
    (do
      (println set-voicing-group)
      #_set-voicing-group transposed-set)))

(voice-and-transpose-rand-set tetrachords 0 #_(rand-int 12))

(defn voice-and-transpose-tetra-probs [tn-level]
  (let [set (tetra-probs2 0.8 0.5 0.2 0.1)
        voiced-set (map #(+ (rand-nth [36 48 60 72]) %) set)
	visible-pairings (map #(list % (mod % 12)) voiced-set)
        transposed-set (map #(+ tn-level %) voiced-set)
	paired-transposed-set (map #(list (mod % 12) (+ tn-level %)) (sort voiced-set))
        set-voicing-group (list :set set
                                :tn-level tn-level
				:reg visible-pairings
                                :sorted-trans paired-transposed-set)]
    (do
      (println set-voicing-group)
      #_set-voicing-group transposed-set)))

(defn play-piano-chord [a-chord]
  (doseq [note a-chord] (sampled-piano note)))

(defn piano-dissonances1 [set-type]
  (let [notes (vec (voice-and-transpose-rand-set ; voicing
                 set-type ;tetrachords set-type, for instance
                       (rand-int 12)))]
    (play-piano-chord notes)))

(defn piano-dissonances3 []
  (let [notes (vec (voice-and-transpose-tetra-probs  ; voicing
                    (rand-int 12)))]
    (play-quieter-piano-chord notes)))

(piano-dissonances3)

(piano-dissonances1 pentachords)

(piano-dissonances1 hexachords)

(defn tetrachord-piano-dissonances2 []
   (let [notes (voice-and-transpose-rand-set ; voicing
                 tetrachords ;set-type, for instance
                       (rand-int 12))]
    (play-piano-chord notes)))

(tetrachord-piano-dissonances2 )

(def metro (metronome 10))

(defn play-piano-dissonances2 [nome]
  (let [beat (nome)]
    (at (nome beat) (tetrachord-piano-dissonances2))
    (apply-at (nome (inc beat)) play-piano-dissonances2 nome [])))

(defn play-piano-dissonances3 [nome]
  (let [beat (nome)]
    (at (nome beat) (piano-dissonances3))
    (apply-at (nome (inc beat)) play-piano-dissonances3 nome [])))


;(play-piano-dissonances2 metro)
(play-piano-dissonances3 metro)

(do (println '(0 1 2)))
