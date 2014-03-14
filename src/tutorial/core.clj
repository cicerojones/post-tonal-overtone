(stop)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; very basics
;;
;;
;; recommended to write the functions (or bind kmacros) to play
;; and stop any function, after evaluating the 'live' package
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(ns tutorial.core)

(use 'overtone.live)

(definst foo [] (saw 200))

(foo)
;; you can stop a particular instrument
(kill foo)

;; to view documentation
(odoc saw)

;; to view documentation in the REPL you must first
;; evaluate (use 'overtone.live) in the REPL, as well, for some reason

;; you can create an instrument that takes a frequency argument with a
;; default value

(definst bar [freq 220] (saw freq))

(bar)
(bar 110)

;; you can also change the amplitude by scaling the signal
(definst baz [freq 435 amp 0.75] (* amp (saw freq)))

(baz 220)
(baz 110 0.7)


;; these all runs concurrently
(foo)
(bar)
(baz)

;; map this saw instrument over a vector of frequencies
(map baz [660 770 880 990 1100])

;; higher and higher, now passed in as a list
(map baz '(700 800 900 1000 1100 1200 1300 1400 1500))

;; you can use other kinds of wave-types, such as a sine wave
(definst quux [freq 440] (* 0.3 (sin-osc freq)))

(quux)

(map quux [660 770 880 990 1100 1210])

;; CTL needs an argument that is already active
(ctl quux :freq 460)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; start varying time/amplitude domain etc.
;;
;;
;; here you need to learn about LINE and ASDR
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;; start modifying the signal with ugens
;; passing arguments for rate and depth, as well as LENGTH,
;; which is used by the LINE
;;
;; additionally tremolo is produced by doing some
;;
;; MODULATION! (what kind, and how/where?)

(definst trem [freq 440 depth 10 rate 6 length 3 amp 0.5]
    (* amp
       (line:kr 0 1 length FREE)
       (saw (+ freq (* depth (sin-osc:kr rate))))))

(trem)
(trem 60 30 0.7 6 0.9)
(trem 200 60 0.3 10 1)

;; can be called with explicit keywords to change arguments
(trem :freq 100 :depth 1 :rate 3 :length 5)

;; umm, feedback-filter?
;; add distortion to a sine wave with this special
;; SIN-OSC-FB, which takes a feedback argument
(defn dem-sin
  [hz fb-flt]
  (demo (sin-osc-fb hz fb-flt)))

(dem-sin 200 3)

(definst sin-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4] 
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (sin-osc freq)
     vol))

(definst sin-wave2 [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4 length 3] 
  (* (env-gen (lin attack sustain release) 1 1 0 length FREE)
     (sin-osc freq)
     vol))
;; one-second of sine, with an ADSR built-in
(sin-wave)

;; 3 seconds of sine, now with an argument for length
(sin-wave2)

;; now 5 seconds, with more ADSR modification
(sin-wave2 :attack 0.1 :sustain 0.15 :release 0.25 :length 5)

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

(noisey)

;; frequencies don't affect a pink-noise object, do they? but you can
;; still change the ADSR/envelope

(definst noisey2 [attack 0.01 sustain 0.4 release 0.1 vol 0.4 length 3] 
  (* (env-gen (lin attack sustain release) 1 1 0 length FREE)
     (pink-noise) ; also have (white-noise) and others...
     vol))

(noisey2)
(noisey2 :attack 0.15 :sustain 0.2 :release 0.3 :vol 0.3 :length 10)

;; for background-noise masking
(definst noisey-long [vol 0.5] 
  (* (pink-noise) ; also have (white-noise) and others...
     vol))


(noisey)

;; play pink-noise indefinitely
(noisey-long 1)


;; now a new "ugen", an LF-TRI
(definst triangle-wave [freq 440 attack 0.01 sustain 0.1 release 0.4 vol 0.4] 
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (lf-tri freq)
     vol))

(triangle-wave)

;; iphase - Initial phase offset. For efficiency 
;;            reasons this is a value ranging from 0 
;;            to 2. 

;;   The triangle wave shape features two linear slopes and is 
;;   not as harmonically rich as a sawtooth wave since it only 
;;   contains odd harmonics (partials). Ideally, this type of 
;;   wave form is mixed with a sine, square or pulse wave to 
;;   add a sparkling or bright effect to a sound and is often 
;;   employed on pads to give them a glittery feel. 

(definst triangle-wave2 [freq 440 iphase 1 attack 0.01 sustain 0.1 release 0.4 vol 0.4] 
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (lf-tri freq iphase)
     vol))

;; ??
(triangle-wave2 :iphase 0.1)


;; interesting undocumented exercise = I.U.E.
(defn make-tri [hz amp]
  (definst triangle-wave [freq hz attack 0.01 sustain 0.1 release 0.4 vol amp] 
    (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
       (lf-tri freq)
       vol)))


(make-tri 200 0.4)


;; now with some more MODULATION, with an lf-pulse
(definst spooky-house [freq 440 width 0.2 
                         attack 0.3 sustain 4 release 0.3 
                         vol 0.4] 
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (sin-osc (+ freq (* 20 (lf-pulse:kr 0.5 0 width))))
     vol))

(spooky-house)

[freq 440.0, iphase 0.0, width 0.5]

  ;; freq   - Frequency in Hertz 
  ;; iphase - Initial phase offset in cycles ( 0..1 ) 
  ;; width  - Pulse width duty cycle from zero to one 

  ;; A non-band-limited pulse oscillator. Outputs a high value 
  ;; of one and a low value of zero. 



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; begin thinking about sequencing and timing
;;
;;                                        
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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

(defn chord-progression-time4 []
  (let [time (now)]
    (at time (player2 baz [*trichords* *tetrachords* *pentachords*] 12))))

(chord-progression-time4)

(definst saw100 [] (saw 100))
(definst saw100v1 [] (* 0.1 (saw 100)))
(definst saw100v2l3 [length 3] ; note: DEFINST (or a "synth") fails with oddp args
  (* 0.2
     (saw 100)
     (line:kr 0 1 length FREE)))
(definst saw100v2l4noline [length 3] ; note: DEFINST (or a "synth") fails with oddp args
  (* 0.2
     (saw 100)
     (line:kr 0.5 1 length FREE)))
(saw100)
(saw100v1)
(saw100v2l3)
(saw100v2l4noline)


(triangle-player)
;; the 'timing' mechanism


(def metro (metronome 128))

(metro)


;;; these belong together
(now)

(definst saw-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4] 
  (* (env-gen (env-lin attack sustain release) 1 1 0 1 FREE)
     (saw freq)
     vol))

(defn saw2 [music-note]
  (saw-wave (midi->hz (note music-note))))

(defn saw1 [m]
  (saw-wave (midi->hz m)))

(saw1 60)
  
(defn play-chord [a-chord]
  (doseq [note a-chord] (saw2 note)))

(defn chord-progression-time []
  (let [time (now)]
    (at time (noisey2))
    (at (+ 2000 time) (play-chord (chord :G3 :major)))
    (at (+ 3000 time) (play-chord (chord :F3 :sus4)))
    (at (+ 4300 time) (play-chord (chord :F3 :major)))
    (at (+ 5000 time) (play-chord (chord :G3 :major)))))

(chord-progression-time)

(defn chord-progression-time2 []
  (let [time (now)]
    (at time (noisey2))
    (at (+ 3000 time) (noisey2 :attack 0.15 :sustain 0.2 :release 0.3 :vol 0.3 :length 10))
    (at (+ 13000 time) (noisey2 :length 1))
    (at (+ 14000 time) (noisey2 :attack 0.14 :sustain 0.2 :release 0.15 :length 3))
    (at (+ 17000 time) (noisey2 :attack 0.15 :sustain 0.2 :release 0.3 :vol 0.3 :length 10))))

(chord-progression-time2)


(defn chord-progression-time3 [r1 r2 v1 v2]
  (let [time (now)]
    (at time (noisey2 :attack 0.15 :sustain 0.2 :release r1 :vol v1 :length 10))
    (at (+ 10000 time) (noisey2 :attack 0.15 :sustain 0.2 :release r2 :vol v2 :length 10))))

(chord-progression-time3 1 1 0.5 0.3)
(chord-progression-time3 0.5 0.3 0.7 1)

(defn chord-progression-time4 [inst]
  (let [time (now)]
    (at time (inst :attack 0.15 :sustain 0.2 :release 0.7 :vol 0.5 :length 10))))



;;; modified version


;; => current beat number

(def metro100 (metronome 100))
(metro100)


;; created a messed-up version of PLAYER
(defn player1 [beat]
  (at (metro beat) (triangle-player))
  (apply-at (metro (inc beat)) #'player1 (inc beat) []))

(at (metro 

(player1 (metro))


;; ((0 1 2 5 6) (72 61 38 65 78) 8 (80 69 46 73 86))
;; ((0 3 4 5 8) (60 51 40 77 56) 8 (68 59 48 85 64))
((0 2 4 7 9) (72 62 52 67 57) 1 (73 63 53 68 58))
((0 1 4 7 8) (60 61 64 79 44) 8 (68 69 72 87 52))
((0 1 2 6 8) (60 73 38 42 80) 8 (68 81 46 50 88))
((0 3 4 7) (48 63 40 55) 0 (48 63 40 55))
((0 1 4) (72 37 52) 1 (73 38 53))



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
