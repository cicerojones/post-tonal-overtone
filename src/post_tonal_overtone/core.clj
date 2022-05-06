(ns post-tonal-overtone.core
  (:require [me.raynes/fs :as fs]))

*ns*

(use 'overtone.live)

(definst noisey2 [attack 0.01 sustain 0.4 release 0.1 vol 0.4 length 3] 
  (* (env-gen (lin attack sustain release) 1 1 0 length FREE)
     (pink-noise) ; also have (white-noise) and others...
     vol))

(noisey2)

(noisey2 :attack 0.15 :sustain 0.2 :release 0.3 :vol 0.3 :length 10)

(definst noisey-sustained [vol 0.5] 
  (* (pink-noise) ; also have (white-noise) and others...
     vol))

(noisey-sustained 1)

;;; what is the correct namespace here?

;; This says to look at post_tonal_overtone/set-class-data.clj
;; which doesn't exist now.
;; (load "set-class-data")

(load "data/set_class_data")
;; (first *tetrachords*) ;(0 1 2 3)
(first post_tonal_overtone.data.set_class_data/tetrachords-tn)


;; note that these functions will print out,
;; to both the cider-repl and the lein terminal repl, apparently
(defn voice-rand-set [set-type]
  (let [set (rand-nth set-type)
        voiced-set (map #(+ (rand-nth [36 48 60 72]) %) set)
        set-voicing-pair (list set voiced-set)]
    (do
      (println set-voicing-pair)
      set-voicing-pair)))

;; (voice-rand-set post_tonal_overtone.data.set_class_data/tetrachords-tn)

(defn voice-and-transpose-rand-set [set-type tn-level]
  (let [set (rand-nth set-type)
        voiced-set (map #(+ (rand-nth [36 48 60 72]) %) set)
        transposed-set (map #(+ tn-level %) voiced-set)
        set-voicing-group (list set voiced-set tn-level transposed-set)]
    (do
      (println set-voicing-group)
      (last set-voicing-group))))

(definst sin3 [freq 880 attack 0.1 sustain 0.15 release 0.25 vol 0.4 length 5]
  (* (env-gen (lin attack sustain release) 1 1 0 length FREE)
     (sin-osc freq)
     vol))

(definst saw1 [freq 880 attack 0.1 sustain 0.15 release 0.25 vol 0.4 length 5]
  (* (env-gen (lin attack sustain release) 1 1 0 length FREE)
     (saw freq)
     vol))

(defn play-chord-sin2 [a-chord]
  (doseq [note a-chord] (sin3 (midi->hz note))))

(sin3 330)
(sin3 (midi->hz 96))
(play-chord-sin2 [64 76 81 86 91 96 101])

(definst med96 [] (* 0.01 (sin-osc (midi->hz 96))))
(med96)

(defn chord-progression-time1 []
  (let [time (now)]
    (at time          (play-chord-sin2 [68 81 46 50 88]))
    (at (+ 2000 time) (play-chord-sin2 [80 69 46 73 86]))
    (at (+ 4000 time) (play-chord-sin2 [68 59 48 85 64]))
    (at (+ 6000 time) (play-chord-sin2 [68 69 72 87 52]))
    (at (+ 8000 time) (play-chord-sin2 [89 55 45 60 62]))))
                                  
(chord-progression-time1)


;; 10-second sine wave
;; uh, rename?
(defn chord-progression-time2 [inst]
  (let [time (now)]
    (at time (inst :attack 0.15 :sustain 0.2 :release 0.4 :vol 0.5 :length 10))))

(chord-progression-time2 sin3)
(chord-progression-time2 saw1)

(defn sine-tetra-diss []
  (doseq [notes (voice-and-transpose-rand-set ; voicing
                 post_tonal_overtone.data.set_class_data/tetrachords-tn
                       (rand-int 12))]
    (sin3 (midi->hz notes))))

;;;; candidates for development with history writing code
;;; this is one for calling interactively
(sine-tetra-diss)

;;; listen to (and print out info about) pentachords 
(map saw1 (map #(midi->hz %) (last (voice-rand-set post_tonal_overtone.data.set_class_data/pentachords-tn))))

;;;; from here on, begin working with a metronome trigger for timing
(def metro (metronome 60))

;; the best one yet (recursive)
(defn chord-progression-time8 [nome]
  (let [beat (nome)]
    (at (nome beat) (sine-tetra-diss))
    (apply-at (nome (inc beat)) chord-progression-time8 nome [])))

;; to paraphrase "A Foggy Day", how long can this thing last?
(chord-progression-time8 metro)




;; apply-at appears to cause problems when attempting to
;; call a functions that takes more than one argument
(defn chord-progression-time3 [nome]
  (let [beat (nome)]
    (at (nome beat)
        (doseq [note (rand-nth [[60 61 62] [60 49 51 55 44]])]
          (saw1 (midi->hz note))))
    (apply-at (nome (inc beat)) chord-progression-time3 nome [])))

(chord-progression-time3 metro)


;; loops through random tetrachords without printing
(defn chord-progression-time4 [nome]
  (let [beat (nome)]
    (at (nome beat)
        (doseq [notes (voice-and-transpose-rand-set ; voicing
                       post_tonal_overtone.data.set_class_data/tetrachords-tn
                       (rand-int 12))]
          (saw1 (midi->hz notes))))
    (apply-at (nome (inc beat)) chord-progression-time4 nome [])))

(chord-progression-time4 metro)

(def some-midis [[72 61 50 46] [60 49 51 55 44] [74 63 52 68] [42 79 70 61]])

;; hack to get around the apply-at problem
(defn chord-progression-time5 [nome]
  (let [beat (nome)]
    (at (nome beat)
        (doseq [note (rand-nth some-midis)]
          (saw1 (midi->hz note))))
    (apply-at (nome (inc beat)) chord-progression-time5 nome [])))


;;; randomly cycle around the midis chords
(chord-progression-time5 metro)


;;; this should be renamed to saw-tetrachords
(defn saw-diss []
  (doseq [notes (voice-and-transpose-rand-set ; voicing
                 post_tonal_overtone.data.set_class_data/tetrachords-tn
                       (rand-int 12))]
    (saw1 (midi->hz notes))))


(saw-diss)



;; also good, only prints first chord
(defn chord-progression-time6 [nome]
  (let [beat (nome)]
    (at (nome beat) (saw-diss))
    (apply-at (nome (inc beat)) chord-progression-time6 nome [])))

(chord-progression-time6 metro)

;; broken? because of apply-at problem?
(defn chord-progression-time7 [nome sound]
  (let [beat (nome)]
    (at (nome beat) sound)
    (apply-at (nome (inc beat)) chord-progression-time7 nome sound [])))

(chord-progression-time7 metro saw-diss)


(defn looper [sound]    
    (let [beat (metro)]
        (at (metro beat) (sound))
        (apply-at (metro (inc beat)) looper sound [])))

;;; problem?
(looper (play-chord-sin2 '(80 69 46 73 86)))

(defn play-chord-saw1 [a-chord]
  (doseq [note a-chord] (saw1 (midi->hz note))))

(defn chord-progression-time9 []
  (let [time (now)]
    (at time          (play-chord-saw1 [68 81 46 50 88]))
    (at (+ 2000 time) (play-chord-saw1 [80 69 46 73 86]))
    (at (+ 4000 time) (play-chord-saw1 [68 59 48 85 64]))
    (at (+ 6000 time) (play-chord-saw1 [68 69 72 87 52]))
    (at (+ 8000 time) (play-chord-saw1 [89 55 45 60 62]))))
                                  
(chord-progression-time9)

(defn chord-progression-time10 [player-fn]
  (let [time (now)]
    (at (+ 0.00 time) (player-fn [68 81 46 50 88]))
    (at (+ 2000 time) (player-fn [80 69 46 73 86]))
    (at (+ 4000 time) (player-fn [68 59 48 85 64]))
    (at (+ 6000 time) (player-fn [68 69 72 87 52]))
    (at (+ 8000 time) (player-fn [89 55 45 60 62]))))
                                  
(chord-progression-time10 play-chord-saw1)


(LET [TIME (NOW) ]
     (
      (AT (+ 0 TIME) (PLAYER-FN '(1 2 3)))
      (AT (+ 1000 TIME) (PLAYER-FN '(4 5 6)))))


;; downcase
(defn chord-prog-time11 [player-fn]
  (let [TIME (NOW) ] 
       (AT (+ 0 TIME) (PLAYER-FN '(68 81 46 50 88)))
       (AT (+ 1000 TIME) (PLAYER-FN '(80 69 46 73 86)))
       (AT (+ 2000 TIME) (PLAYER-FN '(68 59 48 85 64)))
       (AT (+ 3000 TIME) (PLAYER-FN '(68 69 72 87 52)))
       (AT (+ 4000 TIME) (PLAYER-FN '(89 55 45 60 62)))))

;; all the above code that deals with timings could be broken out to a
;; separate file that deals with timing functions
;; everything that follows is concerned with developing an interactive
;; database of chords

;; actually see post_tonal_definitions.clj for use of atoms

(def little-db {})

(defn write-chord-history [hist-vec new-vec]
        (def chord-history (conj hist-vec new-vec)))

(defn voice-and-transpose-rand-set! [set-type tn-level]
  (let [set (rand-nth set-type)
        voiced-set (map #(+ (rand-nth [36 48 60 72]) %) set)
        transposed-set (map #(+ tn-level %) voiced-set)
        set-voicing-group {:set set :voiced-set voiced-set :tn-level tn-level :transposed-set transposed-set})]
    (do
      (println set-voicing-group)
      (last set-voicing-group)))

(write-chord-history post-tonal-overtone.core/little-db {:pcs (0 3 5 7) :midis-normalized (60 51 65 43) :tn-level 2 :midis-transposed(62 53 67 45)))

(sine-tetra-diss)

(stop)

