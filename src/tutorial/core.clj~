(stop)

(ns tutorial.core)

(use 'overtone.live)

(definst foo [] (saw 200))

(foo)
(kill foo)

(odoc saw)

(definst bar [freq 220] (saw freq))


(bar)
(kill bar)

(definst baz [freq 440] (* 0.1 (saw freq)))

(baz 220)
(kill baz)

(foo) 
(bar)
(baz) 

;; nice 
(map baz [660 770 880 990 1100])


(map baz '(700 800 900 1000 1100 1200 1300 1400 1500))


(definst quux [freq 440] (* 0.1 (sin-osc freq)))

(quux)
(map quux [660 770 880 990 1100 1210])

;; CTL seems to work best if argument is already active
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
(noisey)

(definst triangle-wave [freq 440 attack 0.01 sustain 0.1 release 0.4 vol 0.4] 
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (lf-tri freq)
     vol))

(triangle-wave)

;; interesting undocumented exercise = IUE
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

;(kick)

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
(defn player [beat]
  (at (metro beat) (kick))
  (at (metro (+ 0.5 beat)) (c-hat))
  (at (metro (+ 0.15 beat)) (triangle-wave))
  (apply-at (metro (inc beat)) #'player (inc beat) []))

(player (metro))

(metro-bpm metro 100)
