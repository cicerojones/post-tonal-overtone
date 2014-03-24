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
(definst baz [freq 435 amp 0.7] (* amp (saw freq)))
(baz 220)
(baz 110 0.2)


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
;; CTL needs an argument that is already active
(ctl quux :freq 460) 
(map quux [660 770 880 990 1100 1210])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; start varying time/amplitude domain etc.
;;
;;
;; here you need to learn about LINE and ASDR
;;
;;
;; you will also make more different kinds of "instruments"
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
(trem :freq 60 :depth 30 :rate 0.7 :length 6 :amp 0.2)
(trem 200 60 0.3 10 1)

;; can be called with any number of explicit keywords to change arguments
(trem :freq 100 :depth 1 :rate 3 :length 5)

;; umm, feedback-filter?
;; add distortion to a sine wave with this special
;; SIN-OSC-FB, which takes a feedback argument
;;
;; demo's only use one channel of audio?
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

;; 3 seconds of sine, now with an argument for length, as well as frequency

(sin-wave2)
(sin-wave2 220)
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

;; [freq 440.0, iphase 0.0, width 0.5]

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

;; investigate everything going on here to make a bass-drum sound
;; using a sine wav
(definst kick [freq 120 dur 0.3 width 0.5]
  (let [freq-env (* freq (env-gen (perc 0 (* 0.99 dur))))
        env (env-gen (perc 0.01 dur) 1 1 0 1 FREE)
        sqr (* (env-gen (perc 0 0.01)) (pulse (* 2 freq) width))
        src (sin-osc freq-env)
        drum (+ sqr (* env src))]
    (compander drum drum 0.2 1 0.1 0.01 0.01)))

(kick)
(kick 220)
(kick 100)

;; perc:
;; [attack 0.01, release 1, level 1, curve -4]

;;   Create a percussive envelope description suitable for 
;;   use with the env-gen ugen


;; compare use of perc and ugens here
(definst c-hat [amp 0.8 t 0.04]
  (let [env (env-gen (perc 0.001 t) 1 1 0 1 FREE)
        noise (white-noise)
        sqr (* (env-gen (perc 0.01 0.04)) (pulse 880 0.2))
        filt (bpf (+ sqr noise) 9000 0.5)]
    (* amp env filt)))

(c-hat)

;; what does METRONOME do?
;; what does setting it equal to a VAR do?
(now)
(def metro (metronome 60))

;; what does calling the METRO var with an argument do?
(metro 60)
;; as opposed to simply calling it without an arg
(metro)



;; created a messed-up version of PLAYER
(defn tutorial-player [beat]
  (at (metro beat) (kick))
  (at (metro (+ 0.5 beat)) (c-hat))
  (at (metro (+ 0.15 beat)) (triangle-wave))
  (apply-at (metro (inc beat)) #'tutorial-player (inc beat) []))

(tutorial-player (metro))

;; set the bpm of a given metro, apparently
(metro-bpm metro 100)
(metro-bpm metro 60)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; start making atonal chord progressions
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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


;; make it possible to load a file which defines these
;; VARS separately

(voice-rand-set *pentachords*)
(voice-rand-set (rand-nth [*trichords* *tetrachords*]))


(definst sin-wave2 [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4 length 3] 
  (* (env-gen (lin attack sustain release) 1 1 0 length FREE)
     (sin-osc freq)
     vol))

(definst baz [freq 435 amp 0.7] (* amp (saw freq)))

(sin-wave2 :attack 0.1 :sustain 0.15 :release 0.25 :length 5)
(sin-wave2 :freq 880 :attack 0.1 :sustain 0.15 :release 0.25 :length 5)

(definst sin3 [freq 880 attack 0.1 sustain 0.15 release 0.25 vol 0.4 length 5]
  (* (env-gen (lin attack sustain release) 1 1 0 length FREE)
     (sin-osc freq)
     vol))

(definst saw1 [freq 880 attack 0.1 sustain 0.15 release 0.25 vol 0.4 length 5]
  (* (env-gen (lin attack sustain release) 1 1 0 length FREE)
     (saw freq)
     vol))

(map baz (map #(midi->hz %) (last (voice-rand-set *pentachords*))))
(map sin3 (map #(midi->hz %) (last (voice-rand-set *pentachords*))))
(map saw1 (map #(midi->hz %) (last (voice-rand-set *pentachords*))))


;; now include new transposition levels
(defn voice-and-transpose-rand-set [set-type tn-level]
  (let [set (rand-nth set-type)
        voiced-set (map #(+ (rand-nth [36 48 60 72]) %) set)
        transposed-set (map #(+ tn-level %) voiced-set)
        set-voicing-group (list set voiced-set tn-level transposed-set)]
    (do
      (println set-voicing-group)
      (last set-voicing-group))))

(map baz
     (map #(midi->hz %)
          (voice-and-transpose-rand-set
           (rand-nth [*trichords* *tetrachords* *pentachords*])
           (rand-int 12))))

(map saw1
     (map #(midi->hz %)
          (voice-and-transpose-rand-set
           (rand-nth [*trichords* *tetrachords* *pentachords*])
           (rand-int 12))))


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

(definst baz [freq 440] (* 0.99 (saw freq)))

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

;; must evaluate *variables*

(player2 baz [*trichords* *tetrachords* *pentachords*] 12)
(player2 saw1 [*trichords* *tetrachords* *pentachords*] 12)
(triangle-player)


;; just running through simple definst examples
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




;;; these belong together
(now)

(definst saw-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4] 
  (* (env-gen (env-lin attack sustain release) 1 1 0 1 FREE)
     (saw freq)
     vol))

;; create functions that take midi note arguments
(defn saw2 [music-note]
  (saw-wave (midi->hz (note music-note))))

(defn saw3 [m]
  (saw-wave (midi->hz m)))

(saw3 60)
(saw2 69)


;; another way to get a chord?
(doseq [note [60 61 62]]
  (saw2 note))

(defn play-chord [a-chord]
  (doseq [note a-chord] (saw2 note)))

(defn play-chord-saw [a-chord]
  (doseq [note a-chord] (saw2 note)))

(play-chord-saw [60 61 62])


(rand-nth
 '((80 69 46 73 86) (68 59 48 85 64) (73 63 53 68 58) (68 69 72 87 52) (68 81 46 50 88)))

(defn chord-progression-time []
  (let [time (now)]
    (at time (play-chord [68 81 46 50 88]))
    (at (+ 2000 time) (play-chord [80 69 46 73 86]))
    (at (+ 3000 time) (play-chord [68 59 48 85 64]))
    (at (+ 4300 time) (play-chord [73 63 53 68 58]))
    (at (+ 5000 time) (play-chord [68 69 72 87 52]))))

(chord-progression-time)


(play-chord-saw [68 81 46 50 88])

(defn chord-progression-time3 []
  (let [time (now)]
    (at time (play-chord-saw [68 81 46 50 88]))
    (at (+ 2000 time) (play-chord [80 69 46 73 86]))
    (at (+ 3000 time) (play-chord [68 59 48 85 64]))
    (at (+ 4300 time) (play-chord [73 63 53 68 58]))
    (at (+ 5000 time) (play-chord [68 69 72 87 52]))))
                                  
(chord-progression-time3)

(defn chord-progression-time2 []
  (let [time (now)]
    (at time (noisey2))
    (at (+ 3000 time) (noisey2 :attack 0.15 :sustain 0.2 :release 0.3 :vol 0.3 :length 10))
    (at (+ 13000 time) (noisey2 :length 1))
    (at (+ 14000 time) (noisey2 :attack 0.14 :sustain 0.2 :release 0.15 :length 3))
    (at (+ 17000 time) (noisey2 :attack 0.15 :sustain 0.2 :release 0.3 :vol 0.3 :length 10))))

(chord-progression-time2)


(defn chord-progression-time4 [r1 r2 v1 v2]
  (let [time (now)]
    (at time (noisey2 :attack 0.15 :sustain 0.2 :release r1 :vol v1 :length 10))
    (at (+ 10000 time) (noisey2 :attack 0.15 :sustain 0.2 :release r2 :vol v2 :length 10))))

(chord-progression-time4 1 1 0.5 0.3)
(chord-progression-time4 0.5 0.3 0.7 1)

(defn chord-progression-time5 [inst]
  (let [time (now)]
    (at time (inst :attack 0.15 :sustain 0.2 :release 0.7 :vol 0.5 :length 10))))

(defn chord-progression-time6 [nome]
  (let [beat (nome)]
    (at (nome beat) (doseq [note [60 61 62]]
                      (saw2 note)))
    (apply-at (nome (inc beat)) chord-progression-time5 nome [])))

(chord-progression-time5 sixty-bpm)

(doseq [note (rand-nth [[60 61 62] [64 66 68]])]
  (saw2 note))

(doseq [notes (voice-and-transpose-rand-set ; voicing
               *tetrachords* ; set-type
               (rand-int 12))]
  (saw2 notes))


(voice-and-transpose-rand-set ; voicing
 *tetrachords* ; set-type
 (rand-int 12))

(defn chord-progression-time6 [nome]
  (let [beat (nome)]
    (at (nome beat)
        (doseq [notes (voice-and-transpose-rand-set ; voicing
                       *tetrachords* ; set-type
                       (rand-int 12))]
          (saw2 notes)))
    (apply-at (nome (inc beat)) chord-progression-time5 nome [])))

(chord-progression-time6 sixty-bpm)


(defn saw-diss []
  (doseq [notes (voice-and-transpose-rand-set ; voicing
                       *tetrachords* ; set-type
                       (rand-int 12))]
    (saw2 notes)))


(saw-diss)

(defn saw-diss-print []
  (doseq [notes (voice-and-transpose-rand-set ; voicing
                       *tetrachords* ; set-type
                       (rand-int 12))]

    (saw2 notes)))

(saw-diss-print)

(defn chord-progression-time7 [nome]
  (let [beat (nome)]
    (at (nome beat) (saw-diss))
    (apply-at (nome (inc beat)) chord-progression-time7 nome [])))

(chord-progression-time7 sixty-bpm)

(defn chord-progression-time8 [nome sound]
  (let [beat (nome)]
    (at (nome beat) sound)
    (apply-at (nome (inc beat)) #'chord-progression-time8 nome sound [])))

(chord-progression-time8 sixty-bpm saw-diss)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; models
(definst saw-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4] 
  (* (env-gen (env-lin attack sustain release) 1 1 0 1 FREE)
     (saw freq)
     vol))

(defn saw2 [music-note]
  (saw-wave (midi->hz (note music-note))))


;; actually used definition
(definst sin-wave3 [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4 length 3] 
  (* (env-gen (lin attack sustain release) 1 1 0 length FREE)
     (sin-osc freq)
     vol))


;; an envelopped sine-wav player built on the sin-wave3 inst
(defn sin3 [m]
  (sin-wave3 (midi->hz m) :attack 0.1 :sustain 0.15 :release 0.25 :length 5))

(sin3 60)

;; play random chords using the sin3 function, 
(defn sine-diss []
  (doseq [notes (voice-and-transpose-rand-set ; voicing
                       *tetrachords* ; set-type
                       (rand-int 12))]
    (sin3 notes)))


(sine-diss)


(def sine-metro (metronome 60))

(defn chord-progression-time9 [nome]
  (let [beat (nome)]
    (at (nome beat) (sine-diss))
    (apply-at (nome (inc beat)) #'chord-progression-time9 nome [])))

(chord-progression-time9 sine-metro)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; metronome and sequencing
;;
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; setup a sound for our metronome to use

(def kick (sample (freesound-path 2086)))
(def chd1 (play-chord '(60 61 62)))

(chd1)
(sample (freesound-path 2086))
  

; setup a tempo for our metronome to use
(def one-twenty-bpm (metronome 120))
(def sixty-bpm (metronome 60))
(one-twenty-bpm)

(sixty-bpm (sixty-bpm))

; this function will play our sound at whatever tempo we've set our metronome to 
(defn looper [nome sound]    
    (let [beat (nome)]
        (at (nome beat) (sound))
        (apply-at (nome (inc beat)) looper nome sound [])))

; turn on the metronome
(looper one-twenty-bpm kick)
(looper one-twenty-bpm triangle-wave)
(looper one-twenty-bpm (play-chord '(80 69 46 73 86)))
(stop)

triangle-wave
(triangle-wave)

;; doesn't work?
(definst ply-diss [ms 80]
  (play-chord (list ms)))

(ply-diss)

;; to get a feel for how the metronome works, try defining one at the REPL

(def nome (metronome 200))
(nome)
; 8 
; why is this 8? shouldn't it be 1? let's try it again
(nome)
                                        ;140

; whoah, it's almost like it's ticking in the background. it is, in
; fact, ticking in the background. a "beat" is just convenient way to
; represent a timestamp. leave your metronome defined at the REPL, and
; the beat number will steadily increase, even if you aren't using the
; object for anything.

;; CHORD_LIST
;; ((0 1 2 5 6) (72 61 38 65 78) 8 (80 69 46 73 86))
;; ((0 3 4 5 8) (60 51 40 77 56) 8 (68 59 48 85 64))
;; ((0 2 4 7 9) (72 62 52 67 57) 1 (73 63 53 68 58))
;; ((0 1 4 7 8) (60 61 64 79 44) 8 (68 69 72 87 52))
;; ((0 1 2 6 8) (60 73 38 42 80) 8 (68 81 46 50 88))
;; ((0 3 4 7) (48 63 40 55) 0 (48 63 40 55))
;; ((0 1 4) (72 37 52) 1 (73 38 53))
;; ((0 1 3 7 8) (60 73 75 79 44))
;; ((0 1 3 5 8) (72 37 75 53 56))
;; ((0 2 4) (72 62 64) 10 (82 72 74))
;; ((0 1 3 7 8) (60 49 51 55 44))
;; ((0 1 3 4 8) (60 49 75 64 80))
;; ((0 1 3 5 8) (72 73 75 53 80))

;; -------------------------
;; overtone.live/apply-at
;; ([ms-time f args* argseq])
;;   Scheduled function appliction. Works identically to apply, except
;;    that it takes an additional initial argument: ms-time. If ms-time is
;;    in the future, function application is delayed until that time, if
;;    ms-time is in the past function application is immediate.

;;    If you wish to apply slightly before specific time rather than
;;    exactly at it, see apply-by.

;;    Can be used to implement the 'temporal recursion' pattern. This is
;;    where a function has a call to apply-at at its tail:

;;    (defn foo
;;      [t val]
;;      (println val)
;;      (let [next-t (+ t 200)]
;;        (apply-at next-t #'foo [next-t (inc val)])))

;;    (foo (now) 0) ;=> 0, 1, 2, 3...

;;    The fn foo is written in a recursive style, yet the recursion is
;;    scheduled for application 200ms in the future. By passing a function
;;    using #'foo syntax instead of the symbole foo, when later called by
;;    the scheduler it will lookup based on the symbol rather than using
;;    the instance of the function defined earlier. This allows us to
;;    redefine foo whilst the temporal recursion is continuing to execute.

;;    To stop an executing temporal recursion pattern, either redefine the
;;    function to not call itself, or use (stop).
   
(defn temporal-recursion
  [t val]
  (println val)
  (let [next-t (+ t 200)]
    (apply-at next-t #'foo [next-t (inc val)])))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; using midi connected devices
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(connected-midi-devices)

(definst steel-drum [note 60 amp 0.8]
  (let [freq (midicps note)]
    (* amp
       (env-gen (perc 0.01 0.2) 1 1 0 1 :action FREE)
       (+ (sin-osc (/ freq 2))
          (rlpf (saw freq) (* 1.1 freq) 0.4)))))

;; Define a player that connects midi input to that instrument.

(def player (midi-poly-player steel-drum))

;; When you want to stop or change sounds, use midi-player-stop.

(midi-player-stop)

(definst ding
      [note 60 velocity 100 gate 1]
      (let [freq (midicps note)
            amp  (/ velocity 127.0)
            snd  (sin-osc freq)
            env  (env-gen (adsr 0.001 0.1 0.6 0.3) gate :action FREE)]
        (* amp env snd)))

(def dinger (midi-poly-player ding))

(event-debug-on)
;; To stop:

(event-debug-off)
;; You should see that for each key press, there are two events. A
;; general midi control change event:

[:midi :note-on]
;; and a device-specific event i.e.:

[:midi-device Evolution Electronics Ltd. Keystation 61e Keystation 61e :note-on]

;; For simplicity use the general event type:

(on-event [:midi :note-on]
          (fn [e]
            (let [note (:note e)
                  vel  (:velocity e)]
              (your-instr note vel)))
          ::keyboard-handler)


:control-change 17
;; The last argument is a keyword which can be used to refer to this
;; handler, so you can later do:

(remove-handler ::keyboard-handler)


;;; data should be stored separately and LOAD-FILEd
(load "set-class-data")
(first *dyads-tn*)

(stop)
