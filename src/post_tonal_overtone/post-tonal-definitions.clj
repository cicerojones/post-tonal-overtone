(ns post-tonal-overtone.core)

(definst noisey2 [attack 0.01 sustain 0.4 release 0.1 vol 0.4 length 3] 
  (* (env-gen (lin attack sustain release) 1 1 0 length FREE)
     (pink-noise) ; also have (white-noise) and others...
     vol))

(definst noisey-sustained [vol 0.5] 
  (* (pink-noise) ; also have (white-noise) and others...
     vol))

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

(definst saw1 [freq 880 attack 0.1 sustain 0.15 release 0.25 vol 0.4 length 5]
  (* (env-gen (lin attack sustain release) 1 1 0 length FREE)
     (saw freq)
     vol))

(defn play-chord-sin2 [a-chord]
  (doseq [note a-chord] (sin3 (midi->hz note))))

(definst med96 [] (* 0.01 (sin-osc (midi->hz 96))))

(defn chord-progression-time1 []
  (let [time (now)]
    (at time          (play-chord-sin2 [68 81 46 50 88]))
    (at (+ 2000 time) (play-chord-sin2 [80 69 46 73 86]))
    (at (+ 4000 time) (play-chord-sin2 [68 59 48 85 64]))
    (at (+ 6000 time) (play-chord-sin2 [68 69 72 87 52]))
    (at (+ 8000 time) (play-chord-sin2 [89 55 45 60 62]))))

(defn chord-progression-time2 [inst]
  (let [time (now)]
    (at time (inst :attack 0.15 :sustain 0.2 :release 0.4 :vol
                   0.5 :length 10))))

(defn sine-tetra-diss []
  (doseq [notes (voice-and-transpose-rand-set ; voicing
                       *tetrachords* ; set-type
                       (rand-int 12))]
    (sin3 (midi->hz notes))))

(def metro (metronome 60))

(defn chord-progression-time8 [nome]
  (let [beat (nome)]
    (at (nome beat) (sine-tetra-diss))
    (apply-at (nome (inc beat)) chord-progression-time8 nome [])))

(defn chord-progression-time3 [nome]
  (let [beat (nome)]
    (at (nome beat)
        (doseq [note (rand-nth [[60 61 62] [60 49 51 55 44]])]
          (saw1 (midi->hz note))))
    (apply-at (nome (inc beat)) chord-progression-time3 nome [])))

(defn chord-progression-time4 [nome]
  (let [beat (nome)]
    (at (nome beat)
        (doseq [notes (voice-and-transpose-rand-set ; voicing
                       *tetrachords* ; set-type
                       (rand-int 12))]
          (saw1 (midi->hz notes))))
    (apply-at (nome (inc beat)) chord-progression-time4 nome [])))

(defn chord-progression-time5 [nome]
  (let [beat (nome)]
    (at (nome beat)
        (doseq [note (rand-nth some-midis)]
          (saw1 (midi->hz note))))
    (apply-at (nome (inc beat)) chord-progression-time5 nome [])))

(def some-midis [[72 61 50 46] [60 49 51 55 44] [74 63 52 68] [42 79
                                                               70 61]])

(defn saw-diss []
  (doseq [notes (voice-and-transpose-rand-set ; voicing
                       *tetrachords* ; set-type
                       (rand-int 12))]
    (saw1 (midi->hz notes))))

(defn chord-progression-time6 [nome]
  (let [beat (nome)]
    (at (nome beat) (saw-diss))
    (apply-at (nome (inc beat)) chord-progression-time6 nome [])))

(defn chord-progression-time7 [nome sound]
  (let [beat (nome)]
    (at (nome beat) sound)
    (apply-at (nome (inc beat)) chord-progression-time7 nome sound
              [])))

(defn looper [sound]    
    (let [beat (metro)]
        (at (metro beat) (sound))
        (apply-at (metro (inc beat)) looper sound [])))

(defn play-chord-saw1 [a-chord]
  (doseq [note a-chord] (saw1 (midi->hz note))))

(defn chord-progression-time9 []
  (let [time (now)]
    (at time          (play-chord-saw1 [68 81 46 50 88]))
    (at (+ 2000 time) (play-chord-saw1 [80 69 46 73 86]))
    (at (+ 4000 time) (play-chord-saw1 [68 59 48 85 64]))
    (at (+ 6000 time) (play-chord-saw1 [68 69 72 87 52]))
    (at (+ 8000 time) (play-chord-saw1 [89 55 45 60 62]))))

(defn chord-progression-time10 [player-fn]
  (let [time (now)]
    (at (+ 0.00 time) (player-fn [68 81 46 50 88]))
    (at (+ 2000 time) (player-fn [80 69 46 73 86]))
    (at (+ 4000 time) (player-fn [68 59 48 85 64]))
    (at (+ 6000 time) (player-fn [68 69 72 87 52]))
    (at (+ 8000 time) (player-fn [89 55 45 60 62]))))

(defn chord-prog-time11 [player-fn]
  (let [TIME (NOW) ] 
       (AT (+ 0 TIME) (PLAYER-FN '(68 81 46 50 88)))
       (AT (+ 1000 TIME) (PLAYER-FN '(80 69 46 73 86)))
       (AT (+ 2000 TIME) (PLAYER-FN '(68 59 48 85 64)))
       (AT (+ 3000 TIME) (PLAYER-FN '(68 69 72 87 52)))
       (AT (+ 4000 TIME) (PLAYER-FN '(89 55 45 60 62)))))

