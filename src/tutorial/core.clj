(stop)

(ns tutorial.core)

(use 'overtone.live)

(definst foo [] (saw 200))

(foo)
(kill foo)

(odoc saw)


;; give the instrument a frequency argument with a default value 

(definst bar [freq 220] (saw freq))

(bar)
(kill bar)

;; scale the signal
(definst baz [freq 440] (* 0.05 (saw freq)))

(baz 220)
(kill baz)

;; these all runs concurrently
(foo) 
(bar)
(baz) 

;; nice 
(map baz [660 770 880 990 1100])

(map baz '(700 800 900 1000 1100 1200 1300 1400 1500))

(definst quux [freq 440] (* 0.1 (sin-osc freq)))

(quux)
(map quux [660 770 880 990 1100 1210])

;; CTL needs an argument that is already active
(ctl quux :freq 660)

(definst trem [freq 440 depth 10 rate 6 length 3]
    (* 0.3
       (line:kr 0 1 length FREE)
       (saw (+ freq (* depth (sin-osc:kr rate))))))

;; (defn foo
;;   "I don't do a whole lot."
;;   [x]
;;   (println x "Hello, World!"))

(trem 60 30 0.7)

(trem)
(trem 200 60 0.3)


(defn dem-sin
  [hz fb-flt]
  (demo (sin-osc-fb hz fb-flt)))

(definst sin-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4] 
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (sin-osc freq)
     vol))
(sin-wave)

(definst saw-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4] 
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (saw freq)
     vol))
(saw-wave)

(definst square-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4] 
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (lf-pulse:ar freq)
     vol))
(square-wave)

(definst noisey [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4] 
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (pink-noise) ; also have (white-noise) and others...
     vol))

;; for background-noise masking
(definst noisey-long [vol 0.1] 
  (* (pink-noise) ; also have (white-noise) and others...
     vol))


(noisey)
(noisey-long)

(definst triangle-wave [freq 440 attack 0.01 sustain 0.1 release 0.4 vol 0.4] 
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (lf-tri freq)
     vol))

(triangle-wave)

;; interesting undocumented exercise = I.U.E.
(defn make-tri [hz amp]
  (definst triangle-wave [freq hz attack 0.01 sustain 0.1 release 0.4 vol amp] 
    (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
       (lf-tri freq)
       vol)))

(definst rand-tri [rand-ceiling 750 attack 0.01 sustain 0.1 release 0.4 vol 0.4]
  (let [rand-hz (+ 30 (* 10 (rand rand-ceiling)))]
    (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
       (lf-tri rand-hz)
       vol)))

(definst rand-tri2 [rand-ceiling 750 attack 0.01 sustain 0.1 release 0.4 vol 0.4]
  (lf-tri )))

(rand-tri2 :rand-ceiling 500)

    

(make-tri 200 0.4)

(triangle-wave)  

(definst spooky-house [freq 440 width 0.2 
                         attack 0.3 sustain 4 release 0.3 
                         vol 0.4] 
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (sin-osc (+ freq (* 20 (lf-pulse:kr 0.5 0 width))))
     vol))

(definst kick [freq 120 dur 0.3 width 0.5]
  (let [freq-env (* freq (env-gen (perc 0 (* 0.99 dur))))
        env (env-gen (perc 0.01 dur) 1 1 0 1 FREE)
        sqr (* (env-gen (perc 0 0.01)) (pulse (* 2 freq) width))
        src (sin-osc freq-env)
        drum (+ sqr (* env src))]
    (compander drum drum 0.2 1 0.1 0.01 0.01)))

(kick)

(definst c-hat [amp 0.8 t 0.04]
  (let [env (env-gen (perc 0.001 t) 1 1 0 1 FREE)
        noise (white-noise)
        sqr (* (env-gen (perc 0.01 0.04)) (pulse 880 0.2))
        filt (bpf (+ sqr noise) 9000 0.5)]
    (* amp env filt)))

(def metro (metronome 128))

(metro)
;; => current beat number

(metro 100)

;; created a messed-up version of PLAYER
(defn tutorial-player [beat]
  (at (metro beat) (kick))
  (at (metro (+ 0.5 beat)) (c-hat))
  (at (metro (+ 0.15 beat)) (triangle-wave))
  (apply-at (metro (inc beat)) #'tutorial-player (inc beat) []))

(tutorial-player (metro))

(metro-bpm metro 100)


;;; pc-set stuff



(map #(+ 60 %) [0 1 3 4])

(map #(+ (rand-nth [24 36 48 60 72]) %) [0 1 3 4])

(defn voice-rand-set [set-type]
  (let [set (rand-nth set-type)
        voiced-set (map #(+ (rand-nth [36 48 60 72]) %) set)
        set-voicing-pair (list set voiced-set)]
    (do
      (println set-voicing-pair)
      set-voicing-pair)))

(voice-rand-set *pentachords-tn*)
(voice-rand-set (rand-nth [*trichords* *tetrachords*]))
(map baz (map #(midi->hz %) (last (voice-rand-set *pentachords-tn*))))

(map baz (map #(midi->hz %) (voice-and-transpose-rand-set (rand-nth [*trichords* *tetrachords* *pentachords*]) (rand-int 12))))

(defn voice-and-transpose-rand-set [set-type tn-level]
  (let [set (rand-nth set-type)
        voiced-set (map #(+ (rand-nth [36 48 60 72]) %) set)
        transposed-set (map #(+ tn-level %) voiced-set)
        set-voicing-group (list set voiced-set tn-level transposed-set)]
    (do
      (println set-voicing-group)
      (last set-voicing-group))))


;;; current-state
;; built-ins: rand-nth, rand-int
;; overtone: midi-hz, saw, definst
;; anonymous: (rand-nth [36 48 60 72]), (+ tn-level)
;; user-defined:: baz:saw

;;; TODO
;; be able to pass any 'instrument' as function that
;; will be mapped over a vector of args in Hz.
;;
;; those args in Hz are the result of being given (or choosing
;; according to some logic) a particular set/set-type, a transposition level, and a voicing
;;; ADDITIONALLY
;; make it possible to sequence these using timing/metronome objects



;;; ULTIMATELY
;; stringing together sequences of such set|tn|voicing triples should
;; be a part of a larger 'compose' function that works with some guidelines



;;; SO
;; begin by defining a new FN that
;; (defn play-something [player set-of-frequencies])
;; (defn create-set(s)-of-frequencies [set-chooser voicing-chooser]
;; (defn set-chooser [set-type-guidelines]
;; (defn voicing-chooser [voicing-guidelines]

;;; the prototypes
;; 'the instrument'

(definst baz [freq 440] (* 0.1 (saw freq)))

(baz)
;; the 'player'

(defn player2 [inst set-types tn-level-ceiling]
  (map inst ; instrument
       (map #(midi->hz %) ; frequency-conversion
            (voice-and-transpose-rand-set ; voicing
             (rand-nth set-types) ; set-type
             (rand-int tn-level-ceiling)))))

                                        ; tn-level

(defn triangle-player []
  (player2 triangle-wave [*trichords* *tetrachords* *pentachords*] 12))

(player2 baz [*trichords* *tetrachords* *pentachords*] 12)

(triangle-player)
;; the 'timing' mechanism


(def metro (metronome 128))

(metro)
;; => current beat number

(metro 100)


;; created a messed-up version of PLAYER
(defn player1 [beat]
  (at (metro beat) (triangle-player))
  (apply-at (metro (inc beat)) #'player1 (inc beat) []))

(player1 (metro))





;;; data should be stored separately and LOAD-FILEd
(def *dyads-tn* '((0 1) (0 2) (0 3) (0 4) (0 5) (0 6)))

(def *trichords-tn* '((0 1 2) (0 1 3) (0 2 3) (0 1 4) (0 3 4) (0 1 5) (0 4 5) (0 1 6) (0 5 6) (0 2 4) (0 2 5) (0 3 5) (0 2 6) (0 4 6) (0 2 7) (0 3 6) (0 3 7) (0 4 7) (0 4 8)))

(def *tetrachords-tn* '((0 1 2 3) (0 1 2 4) (0 2 3 4) (0 1 3 4) (0 1 2 5) (0 3 4 5) (0 1 2 6) (0 4 5 6) (0 1 2 7) (0 1 4 5) (0 1 5 6) (0 1 6 7) (0 2 3 5) (0 1 3 5) (0 2 4 5) (0 2 3 6) (0 3 4 6) (0 1 3 6) (0 3 5 6) (0 2 3 7) (0 4 5 7) (0 1 4 6) (0 2 5 6) (0 1 5 7) (0 2 6 7) (0 3 4 7) (0 1 4 7) (0 3 6 7) (0 1 4 8) (0 3 4 8) (0 1 5 8) (0 2 4 6) (0 2 4 7) (0 3 5 7) (0 2 5 7) (0 2 4 8) (0 2 6 8) (0 3 5 8) (0 2 5 8) (0 3 6 8) (0 3 6 9) (0 1 3 7) (0 4 6 7)))

(def *pentachords-tn* '((0 1 2 3 4) (0 1 2 3 5) (0 2 3 4 5) (0 1 2 4 5) (0 1 3 4 5) (0 1 2 3 6) (0 3 4 5 6) (0 1 2 3 7) (0 4 5 6 7) (0 1 2 5 6) (0 1 4 5 6) (0 1 2 6 7) (0 1 5 6 7) (0 2 3 4 6) (0 1 2 4 6) (0 2 4 5 6) (0 1 3 4 6) (0 2 3 5 6) (0 2 3 4 7) (0 3 4 5 7) (0 1 3 5 6) (0 1 2 4 8) (0 2 3 4 8) (0 1 2 5 7) (0 2 5 6 7) (0 1 2 6 8) (0 1 3 4 7) (0 3 4 6 7) (0 1 3 4 8) (0 1 4 5 7) (0 2 3 6 7) (0 1 3 6 7) (0 1 4 6 7) (0 1 3 7 8) (0 1 5 7 8) (0 1 4 5 8) (0 3 4 7 8) (0 1 4 7 8) (0 2 3 5 7) (0 2 4 5 7) (0 1 3 5 7) (0 2 4 6 7) (0 2 3 5 8) (0 3 5 6 8) (0 2 4 5 8) (0 3 4 6 8) (0 1 3 5 8) (0 3 5 7 8) (0 2 3 6 8) (0 2 5 6 8) (0 1 3 6 8) (0 2 5 7 8) (0 1 4 6 8) (0 2 4 7 8) (0 1 3 6 9) (0 2 3 6 9) (0 1 4 6 9) (0 1 4 7 9) (0 2 4 6 8) (0 2 4 6 9) (0 2 4 7 9) (0 1 2 4 7) (0 3 5 6 7) (0 3 4 5 8) (0 1 2 5 8) (0 3 6 7 8)))

(defvar *hexachords-tn* '((0 1 2 3 4 5) (0 1 2 3 4 6) (0 2 3 4 5 6) (0 1 2 3 5 6) (0 1 3 4 5 6) (0 1 2 4 5 6) (0 1 2 3 6 7) (0 1 4 5 6 7) (0 1 2 5 6 7) (0 1 2 6 7 8) (0 2 3 4 5 7) (0 1 2 3 5 7) (0 2 4 5 6 7) (0 1 3 4 5 7) (0 2 3 4 6 7) (0 1 2 4 5 7) (0 2 3 5 6 7) (0 1 2 4 6 7) (0 1 3 5 6 7) (0 1 3 4 6 7) (0 1 3 4 5 8) (0 3 4 5 7 8) (0 1 2 4 5 8) (0 3 4 6 7 8) (0 1 4 5 6 8) (0 2 3 4 7 8) (0 1 2 4 7 8) (0 1 4 6 7 8) (0 1 2 5 7 8) (0 1 3 6 7 8) (0 1 3 4 7 8) (0 1 4 5 7 8) (0 1 4 5 8 9) (0 2 3 4 6 8) (0 2 4 5 6 8) (0 1 2 4 6 8) (0 2 4 6 7 8) (0 2 3 5 6 8) (0 1 3 4 6 8) (0 2 4 5 7 8) (0 1 3 5 6 8) (0 2 3 5 7 8) (0 1 3 5 7 8) (0 1 3 4 6 9) (0 2 3 5 6 9) (0 1 3 5 6 9) (0 1 3 6 8 9) (0 1 3 6 7 9) (0 2 3 6 8 9) (0 1 3 5 8 9) (0 1 4 6 8 9) (0 2 4 5 7 9) (0 2 3 5 7 9) (0 2 4 6 7 9) (0 1 3 5 7 9) (0 2 4 6 8 9) (0 2 4 6 8 10) (0 1 2 3 4 7) (0 3 4 5 6 7) (0 1 2 3 4 8) (0 1 2 3 7 8) (0 2 3 4 5 8) (0 3 4 5 6 8) (0 1 2 3 5 8) (0 3 5 6 7 8) (0 1 2 3 6 8) (0 2 5 6 7 8) (0 1 2 3 6 9) (0 1 2 5 6 8) (0 2 3 6 7 8) (0 1 2 5 6 9) (0 1 2 5 8 9) (0 2 3 4 6 9) (0 1 2 4 6 9) (0 2 4 5 6 9) (0 1 2 4 7 9) (0 2 3 4 7 9) (0 1 2 5 7 9) (0 1 3 4 7 9) (0 1 4 6 7 9)))

;TnI-types
(def *dyads* '((0 1) (0 2) (0 3) (0 4) (0 5) (0 6)))

(def *trichords* '((0 1 2) (0 1 3) (0 1 4) (0 1 5) (0 1 6) (0 2 4) (0 2 5) (0 2 6) (0 2 7) (0 3 6) (0 3 7) (0 4 8)))

(def *tetrachords* '((0 1 2 3) (0 1 2 4) (0 1 3 4) (0 1 2 5) (0 1 2 6) (0 1 2 7) (0 1 4 5) (0 1 5 6) (0 1 6 7) (0 2 3 5) (0 1 3 5) (0 2 3 6) (0 1 3 6) (0 2 3 7) (0 1 3 7) (0 1 4 6) (0 1 5 7) (0 3 4 7) (0 1 4 7) (0 1 4 8) (0 1 5 8) (0 2 4 6) (0 2 4 7) (0 2 5 7) (0 2 4 8) (0 2 6 8) (0 3 5 8) (0 2 5 8) (0 3 6 9)))

(def *pentachords* '((0 1 2 3 4) (0 1 2 3 5) (0 1 2 4 5) (0 1 2 3 6) (0 1 2 3 7) (0 1 2 5 6) (0 1 2 6 7) (0 2 3 4 6) (0 1 2 4 6) (0 1 3 4 6) (0 2 3 4 7) (0 1 3 5 6) (0 1 2 4 8) (0 1 2 5 7) (0 1 2 6 8) (0 1 3 4 7) (0 1 3 4 8) (0 1 4 5 7) (0 1 3 6 7) (0 1 3 7 8) (0 1 4 5 8) (0 1 4 7 8) (0 2 3 5 7) (0 1 3 5 7) (0 2 3 5 8) (0 2 4 5 8) (0 1 3 5 8) (0 2 3 6 8) (0 1 3 6 8) (0 1 4 6 8) (0 1 3 6 9) (0 1 4 6 9) (0 2 4 6 8) (0 2 4 6 9) (0 2 4 7 9) (0 1 2 4 7) (0 3 4 5 8) (0 1 2 5 8)))

(def *hexachords* '((0 1 2 3 4 5) (0 1 2 3 4 6) (0 1 2 3 5 6) (0 1 2 4 5 6) (0 1 2 3 6 7) (0 1 2 5 6 7) (0 1 2 6 7 8) (0 2 3 4 5 7) (0 1 2 3 5 7) (0 1 3 4 5 7) (0 1 2 4 5 7) (0 1 2 4 6 7) (0 1 3 4 6 7) (0 1 3 4 5 8) (0 1 2 4 5 8) (0 1 4 5 6 8) (0 1 2 4 7 8) (0 1 2 5 7 8) (0 1 3 4 7 8) (0 1 4 5 8 9) (0 2 3 4 6 8) (0 1 2 4 6 8) (0 2 3 5 6 8) (0 1 3 4 6 8) (0 1 3 5 6 8) (0 1 3 5 7 8) (0 1 3 4 6 9) (0 1 3 5 6 9) (0 1 3 6 8 9) (0 1 3 6 7 9) (0 1 3 5 8 9) (0 2 4 5 7 9) (0 2 3 5 7 9) (0 1 3 5 7 9) (0 2 4 6 8 10) (0 1 2 3 4 7) (0 1 2 3 4 8) (0 1 2 3 7 8) (0 2 3 4 5 8) (0 1 2 3 5 8) (0 1 2 3 6 8) (0 1 2 3 6 9) (0 1 2 5 6 8) (0 1 2 5 6 9) (0 2 3 4 6 9) (0 1 2 4 6 9) (0 1 2 4 7 9) (0 1 2 5 7 9) (0 1 3 4 7 9) (0 1 4 6 7 9)))
