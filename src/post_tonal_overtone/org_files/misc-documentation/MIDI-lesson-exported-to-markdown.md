<div id="table-of-contents">
<h2>Table of Contents</h2>
<div id="text-table-of-contents">
<ul>
<li><a href="#orgheadline33">1. session with midi device in overtone</a>
<ul>
<li><a href="#orgheadline1">1.1. start up and find info about overtone and midi devices</a></li>
<li><a href="#orgheadline2">1.2. (midi-connected-devices)</a></li>
<li><a href="#orgheadline3">1.3. (event-debug-on)</a></li>
<li><a href="#orgheadline4">1.4. note-on event with the event-debug-on function</a></li>
<li><a href="#orgheadline5">1.5. note-off event</a></li>
<li><a href="#orgheadline6">1.6. debug off</a></li>
<li><a href="#orgheadline8">1.7. control-change starting point</a>
<ul>
<li><a href="#orgheadline7">1.7.1. knob 1</a></li>
</ul>
</li>
<li><a href="#orgheadline10">1.8. control-change ending point</a>
<ul>
<li><a href="#orgheadline9">1.8.1. knob2</a></li>
</ul>
</li>
<li><a href="#orgheadline11">1.9. create a basic instrument for use with midi-poly-player</a></li>
<li><a href="#orgheadline12">1.10. make a player to use midi-poly-player and defined instrument</a></li>
<li><a href="#orgheadline13">1.11. stop the defined player&#x2013;doesn't do what you hope?</a></li>
<li><a href="#orgheadline14">1.12. create an event handler with on-event&#x2013;needs a real instrument for demo</a></li>
<li><a href="#orgheadline15">1.13. remove event handler</a></li>
<li><a href="#orgheadline16">1.14. create a very simple instrument</a></li>
<li><a href="#orgheadline17">1.15. demonstrate use of defined instrument with defaults</a></li>
<li><a href="#orgheadline18">1.16. kill all instances of running instrument&#x2013;contrast with killing with ID-specific number</a></li>
<li><a href="#orgheadline19">1.17. create a player using midi-poly-player to play the simple instrument</a></li>
<li><a href="#orgheadline20">1.18. stop the player&#x2013;doesn't do what you hope?</a></li>
<li><a href="#orgheadline21">1.19. stop the player by redefining with 'nil'</a></li>
<li><a href="#orgheadline22">1.20. stop all sounds</a></li>
<li><a href="#orgheadline23">1.21. create an event handler using on-event for using with simple-sine instrument</a></li>
<li><a href="#orgheadline24">1.22. stop player using remove-event-handler</a></li>
<li><a href="#orgheadline25">1.23. create an event handler with on-event and use control-change??</a></li>
<li><a href="#orgheadline26">1.24. basically works, but gets loud fast (vel vs. val error)</a></li>
<li><a href="#orgheadline27">1.25. println note-on and velocity message</a></li>
<li><a href="#orgheadline28">1.26. correctly grab and print out the cc-chanel and velocity (use :note and :velocity)</a></li>
<li><a href="#orgheadline29">1.27. correctly grab and use as vol the cc-chanel and velocity (use :note and :velocity)</a></li>
<li><a href="#orgheadline30">1.28. </a></li>
<li><a href="#orgheadline31">1.29. </a></li>
<li><a href="#orgheadline32">1.30. remember, to use ctl, must have an active instance</a></li>
</ul>
</li>
</ul>
</div>
</div>

# session with midi device in overtone<a id="orgheadline33"></a>

## start up and find info about overtone and midi devices<a id="orgheadline1"></a>

    (use 'overtone.core)

Must be captialized?

    overtone.version/OVERTONE-VERSION

## (midi-connected-devices)<a id="orgheadline2"></a>

    (midi-connected-devices)

    ({:description "Axiom A.I.R. Mini32 MIDI",
      :vendor "M-Audio",
      :sinks 0,
      :sources 2147483647,
      :name "MIDI",
      :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0],
      :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"],
      :overtone.studio.midi/dev-num 0,
      :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"],
      :version "Unknown version"}
    
     {:description "Axiom A.I.R. Mini32 HyperControl",
      :vendor "M-Audio",
      :sinks 0,
      :sources 2147483647,
      :name "HyperControl",
      :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "HyperControl" "Axiom A.I.R. Mini32 HyperControl" 0],
      :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x3886ef0a "HyperControl"],
      :overtone.studio.midi/dev-num 0,
      :device #object[com.sun.media.sound.MidiInDevice 0x42fac5ea "com.sun.media.sound.MidiInDevice@42fac5ea"],
      :version "Unknown version"})

## (event-debug-on)<a id="orgheadline3"></a>

    (event-debug-on)

## note-on event with the event-debug-on function<a id="orgheadline4"></a>

    event:  [:midi :note-on] ({:data2 117, :command :note-on, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x23ec8f33 "com.sun.media.sound.FastShortMessage@23ec8f33"], :note 60, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :note-on, :data1 60, :data2-f 0.9212598, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 18402497323, :velocity 117, :velocity-f 0.9212598}) 
    event:  [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0] ({:data2 117, :command :note-on, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x23ec8f33 "com.sun.media.sound.FastShortMessage@23ec8f33"], 
    :note 60, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :note-on, :data1 60, :data2-f 0.9212598, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 18402497323, :velocity 117, :velocity-f 0.9212598})
     
    event:  (:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0 :note-on 60) ({:data2 117, :command :note-on, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x23ec8f33 "com.sun.media.sound.FastShortMessage@23ec8f33"], 
    :note 60, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :note-on, :data1 60, :data2-f 0.9212598, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 18402497323, :velocity 117, :velocity-f 0.9212598})
     
    event:  (:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0 :note-on) ({:data2 117, :command :note-on, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x23ec8f33 "com.sun.media.sound.FastShortMessage@23ec8f33"], 
    :note 60, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :note-on, :data1 60, :data2-f 0.9212598, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 18402497323, :velocity 117, :velocity-f 0.9212598})

## note-off event<a id="orgheadline5"></a>

    event:  [:midi :note-off] ({:data2 0, :command :note-off, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x535e9cfb "com.sun.media.sound.FastShortMessage@535e9cfb"], :note 60, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :note-on, :data1 60, :data2-f 0.0, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 18410464271, :velocity 0, :velocity-f 0.0}) 
    event:  [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0] ({:data2 0, :command :note-off, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x535e9cfb "com.sun.media.sound.FastShortMessage@535e9cfb"], :note 60, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :note-on, :data1 60, :data2-f 0.0, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 18410464271, :velocity 0, :velocity-f 0.0}) 
    event:  (:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0 :note-off 60) ({:data2 0, :command :note-off, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x535e9cfb "com.sun.media.sound.FastShortMessage@535e9cfb"], :note 60, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :note-on, :data1 60, :data2-f 0.0, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 18410464271, :velocity 0, :velocity-f 0.0})
     
    event:  (:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0 :note-off) ({:data2 0, :command :note-off, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x535e9cfb "com.sun.media.sound.FastShortMessage@535e9cfb"], :note 60, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :note-on, :data1 60, :data2-f 0.0, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 18410464271, :velocity 0, :velocity-f 0.0})

## debug off<a id="orgheadline6"></a>

    (event-debug-off)

## control-change starting point<a id="orgheadline8"></a>

### knob 1<a id="orgheadline7"></a>

    event:  [:midi :control-change] ({:data2 1, :command :control-change, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x18025326 "com.sun.media.sound.FastShortMessage@18025326"], :note 2, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :control-change, :data1 2, :data2-f 0.007874016, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 18651991003, :velocity 1, :velocity-f 0.007874016}) 
    event:  [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0] ({:data2 1, :command :control-change, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x18025326 "com.sun.media.sound.FastShortMessage@18025326"], :note 2, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :control-change, :data1 2, :data2-f 0.007874016, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 18651991003, :velocity 1, :velocity-f 0.007874016})
     
    event:  (:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0 :control-change 2) ({:data2 1, :command :control-change, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x18025326 "com.sun.media.sound.FastShortMessage@18025326"], :note 2, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :control-change, :data1 2, :data2-f 0.007874016, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 18651991003, :velocity 1, :velocity-f 0.007874016})
     
    event:  (:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0 :control-change) ({:data2 1, :command :control-change, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x18025326 "com.sun.media.sound.FastShortMessage@18025326"], :note 2, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :control-change, :data1 2, :data2-f 0.007874016, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 18651991003, :velocity 1, :velocity-f 0.007874016})

## control-change ending point<a id="orgheadline10"></a>

    event:  [:midi :control-change] ({:data2 127, :command :control-change, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x42926a68 "com.sun.media.sound.FastShortMessage@42926a68"], :note 2, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :control-change, :data1 2, :data2-f 1.0, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 18652583742, :velocity 127, :velocity-f 1.0}) 
    event:  [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0] ({:data2 127, :command :control-change, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x42926a68 "com.sun.media.sound.FastShortMessage@42926a68"], :note 2, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :control-change, :data1 2, :data2-f 1.0, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 18652583742, :velocity 127, :velocity-f 1.0})
     
    event:  (:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0 :control-change 2) ({:data2 127, :command :control-change, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x42926a68 "com.sun.media.sound.FastShortMessage@42926a68"], :note 2, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :control-change, :data1 2, :data2-f 1.0, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 18652583742, :velocity 127, :velocity-f 1.0})
     
    event:  (:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0 :control-change) ({:data2 127, :command :control-change, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x42926a68 "com.sun.media.sound.FastShortMessage@42926a68"], :note 2, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :control-change, :data1 2, :data2-f 1.0, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 18652583742, :velocity 127, :velocity-f 1.0})

    event:  [:midi :control-change] ({:data2 28, :command :control-change, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x77f7f70a "com.sun.media.sound.FastShortMessage@77f7f70a"], :note 1, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :control-change, :data1 1, :data2-f 0.22047244, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 25614776293, :velocity 28, :velocity-f 0.22047244}) 
    event:  [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0] ({:data2 28, :command :control-change, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x77f7f70a "com.sun.media.sound.FastShortMessage@77f7f70a"], :note 1, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :control-change, :data1 1, :data2-f 0.22047244, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 25614776293, :velocity 28, :velocity-f 0.22047244})
     
    event:  (:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0 :control-change 1) ({:data2 28, :command :control-change, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x77f7f70a "com.sun.media.sound.FastShortMessage@77f7f70a"], :note 1, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :control-change, :data1 1, :data2-f 0.22047244, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 25614776293, :velocity 28, :velocity-f 0.22047244})
     
    event:  (:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0 :control-change) ({:data2 28, :command :control-change, :channel 0, :msg #object[com.sun.media.sound.FastShortMessage 0x77f7f70a "com.sun.media.sound.FastShortMessage@77f7f70a"], :note 1, :dev-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :status :control-change, :data1 1, :data2-f 0.22047244, :device {:description "Axiom A.I.R. Mini32 MIDI", :vendor "M-Audio", :sinks 0, :sources 2147483647, :name "MIDI", :transmitter #object[com.sun.media.sound.MidiInDevice$MidiInTransmitter 0x17af0adf "com.sun.media.sound.MidiInDevice$MidiInTransmitter@17af0adf"], :overtone.studio.midi/full-device-key [:midi-device "M-Audio" "MIDI" "Axiom A.I.R. Mini32 MIDI" 0], :info #object[com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo 0x1142bdd "MIDI"], :overtone.studio.midi/dev-num 0, :device #object[com.sun.media.sound.MidiInDevice 0x117963cc "com.sun.media.sound.MidiInDevice@117963cc"], :version "Unknown version"}, :timestamp 25614776293, :velocity 28, :velocity-f 0.22047244})

### knob2<a id="orgheadline9"></a>

## create a basic instrument for use with midi-poly-player<a id="orgheadline11"></a>

    (definst steel-drum [note 60 amp 0.8]
      (let [freq (midicps note)]
        (* amp
           (env-gen (perc 0.01 0.2) 1 1 0 1 :action FREE)
           (+ (sin-osc (/ freq 2))
              (rlpf (saw freq) (* 1.1 freq) 0.4)))))

## make a player to use midi-poly-player and defined instrument<a id="orgheadline12"></a>

    (def player (midi-poly-player steel-drum))

## stop the defined player&#x2013;doesn't do what you hope?<a id="orgheadline13"></a>

    (midi-player-stop)

## create an event handler with on-event&#x2013;needs a real instrument for demo<a id="orgheadline14"></a>

    (on-event [:midi :note-on]
              (fn [e]
                (let [note (:note e)
                      vel  (:velocity e)]
                  (your-instr note vel)))
              ::keyboard-handler)

## remove event handler<a id="orgheadline15"></a>

    (remove-event-handler ::keyboard-handler)

## create a very simple instrument<a id="orgheadline16"></a>

    (definst boop [note 60 amp 0.3]
      (let [freq (midicps note)]
        (* amp (sin-osc freq))))

## demonstrate use of defined instrument with defaults<a id="orgheadline17"></a>

    (boop)

## kill all instances of running instrument&#x2013;contrast with killing with ID-specific number<a id="orgheadline18"></a>

    (kill boop)

## create a player using midi-poly-player to play the simple instrument<a id="orgheadline19"></a>

    (def booper (midi-poly-player boop))

## stop the player&#x2013;doesn't do what you hope?<a id="orgheadline20"></a>

    (midi-player-stop)

## stop the player by redefining with 'nil'<a id="orgheadline21"></a>

    (def booper (midi-poly-player nil))p

## stop all sounds<a id="orgheadline22"></a>

    (stop)

## create an event handler using on-event for using with simple-sine instrument<a id="orgheadline23"></a>

    (on-event [:midi :note-on]
              (fn [e]
                (let [note (:note e)
                      vel  (:velocity e)]
                  (boop note (* 0.1 vel))))
              ::boop-handler)

## stop player using remove-event-handler<a id="orgheadline24"></a>

    (remove-event-handler ::boop-handler)

## create an event handler with on-event and use control-change??<a id="orgheadline25"></a>

    (definst anoise [vol 0.01]
      (* (pink-noise)
         vol))

    (anoise)

    (kill anoise)

## basically works, but gets loud fast (vel vs. val error)<a id="orgheadline26"></a>

    (on-event [:midi :control-change]
              (fn [e]
                (let [val (:data2 e)
                      vel  (:velocity e)]
                  (anoise vel)))
              ::data-handler)

    (remove-event-handler ::data-handler)

    (on-event [:midi :control-change]
              (fn [e]
                (let [val (:data2 e)
                      vel  (:velocity e)]
                  (ctl anoise val)))
              ::ctldata-handler)

    (remove-event-handler ::ctldata-handler)

## println note-on and velocity message<a id="orgheadline27"></a>

    (on-event [:midi :note-on] (fn [{note :note velocity :velocity}]
                                 (println "Note: " note ", Velocity: " velocity))
              ::note-printer)

    (remove-event-handler ::note-printer)

## correctly grab and print out the cc-chanel and velocity (use :note and :velocity)<a id="orgheadline28"></a>

    (on-event [:midi :control-change] (fn [{cc-channel :note velocity :velocity}]
                                 (println "channel: " cc-channel ", Velocity: " velocity))
              ::cc-printer)

    (remove-event-handler ::cc-printer)

## correctly grab and use as vol the cc-chanel and velocity (use :note and :velocity)<a id="orgheadline29"></a>

    (on-event [:midi :control-change] (fn [{cc-channel :note velocity :velocity}]
                                        (ctl anoise :vol (scale-range velocity 1 127 0 1)))
              ::cc-player)

    (remove-event-handler ::cc-player)

## <a id="orgheadline30"></a>

    (on-event [:midi :control-change] (fn [{cc-channel :note velocity :velocity}]
                                        (lpf (ctl anoise :vol (scale-range velocity 1 127 0 1))
                                             10)
              ::cc-filterplayer)

    (demo 10
      (lpf (* 0.5 (saw [339 440]))
           (mouse-x 10 10000)))

## <a id="orgheadline31"></a>

    (definst an-fnoise [vol 0.1 ffreq 1000]
      (lpf (* (pink-noise)
              vol)
           ffreq))

    (an-fnoise)

    (kill an-fnoise)

    (ctl an-fnoise :ffreq 4000)

## remember, to use ctl, must have an active instance<a id="orgheadline32"></a>

    (an-fnoise)

    (on-event [:midi :control-change] (fn [{cc-channel :note velocity :velocity}]
                                         (ctl an-fnoise :ffreq (scale-range velocity 1 127 100 8000))
                                             10)
              ::cc-filterplayer)

    (on-event [:midi :note-on]
              (fn [m]
                (let [note (:note m)]
                  (prophet :freq (midi->hz note)
                           :decay 5
                           :rq 0.6
                           :cutoff-freq 1000)))
              ::prophet-midi)

    (on-event [:midi :note-on]
              (fn [m]
                (let [note (:note m)]
                  (prophet :freq (midi->hz note)
                           :decay 5
                           :rq 0.6
                           :cutoff-freq 1000
                           :amp (:velocity-f m))))
              ::prophet-midi)

    (on-event [:midi :control-change] (fn [{cc-channel :note velocity :velocity}]
                                        (ctl anoise :vol (scale-range velocity 1 127 0 1)))
              ::cc-player)
