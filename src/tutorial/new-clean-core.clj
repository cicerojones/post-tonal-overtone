(ns tutorial.core)

(use 'overtone.live)

(definst noisey2 [attack 0.01 sustain 0.4 release 0.1 vol 0.4 length 3] 
  (* (env-gen (lin attack sustain release) 1 1 0 length FREE)
     (pink-noise) ; also have (white-noise) and others...
     vol))

(noisey2)

(noisey2 :attack 0.15 :sustain 0.2 :release 0.3 :vol 0.3 :length 10)

(definst noisey-long [vol 0.5] 
  (* (pink-noise) ; also have (white-noise) and others...
     vol))

(noisey-long 1)

(load "set-class-data")

(defn voice-rand-set [set-type]
  (let [set (rand-nth set-type)
        voiced-set (map #(+ (rand-nth [36 48 60 72]) %) set)
        set-voicing-pair (list set voiced-set)]
    (do
      (println set-voicing-pair)
      set-voicing-pair)))

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

(sin3 330)
(sin3 (midi->hz 96))
(play-chord-sin2 [64 76 81 86 91 96 101])

(definst med96 [] (* 0.5 (sin-osc (midi->hz 96))))

(defn play-chord-sin2 [a-chord]
  (doseq [note a-chord] (sin3 (midi->hz note))))

(defn chord-progression-time1 []
  (let [time (now)]
    (at time          (play-chord-sin2 [68 81 46 50 88]))
    (at (+ 2000 time) (play-chord-sin2 [80 69 46 73 86]))
    (at (+ 4000 time) (play-chord-sin2 [68 59 48 85 64]))
    (at (+ 6000 time) (play-chord-sin2 [68 69 72 87 52]))
    (at (+ 8000 time) (play-chord-sin2 [89 55 45 60 62]))))
                                  
(chord-progression-time1)

;; 10-second sine wave
(defn chord-progression-time2 [inst]
  (let [time (now)]
    (at time (inst :attack 0.15 :sustain 0.2 :release 0.7 :vol 0.5 :length 10))))

(chord-progression-time2 sin3)



(defn sine-tetra-diss []
  (doseq [notes (voice-and-transpose-rand-set ; voicing
                       *tetrachords* ; set-type
                       (rand-int 12))]
    (sin3 (midi->hz notes))))

(sine-tetra-diss)

(def metro (metronome 60))

;; the best one yet
(defn chord-progression-time8 [nome]
  (let [beat (nome)]
    (at (nome beat) (sine-tetra-diss))
    (apply-at (nome (inc beat)) chord-progression-time8 nome [])))

(chord-progression-time8 metro)


(definst saw1 [freq 880 attack 0.1 sustain 0.15 release 0.25 vol 0.4 length 5]
  (* (env-gen (lin attack sustain release) 1 1 0 length FREE)
     (saw freq)
     vol))

(map saw1 (map #(midi->hz %) (last (voice-rand-set *pentachords*))))



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
                       *tetrachords* ; set-type
                       (rand-int 12))]
          (saw1 (midi->hz notes))))
    (apply-at (nome (inc beat)) chord-progression-time4 nome [])))

(chord-progression-time4 metro)

;; hack to get around the apply-at problem
(defn chord-progression-time5 [nome]
  (let [beat (nome)]
    (at (nome beat)
        (doseq [note (rand-nth some-midis)]
          (saw1 (midi->hz note))))
    (apply-at (nome (inc beat)) chord-progression-time5 nome [])))

(def some-midis [[72 61 50 46] [60 49 51 55 44] [74 63 52 68] [42 79 70 61]])

(chord-progression-time5 metro)


(defn saw-diss []
  (doseq [notes (voice-and-transpose-rand-set ; voicing
                       *tetrachords* ; set-type
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

(stop)
