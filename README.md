# tutorial

Exploration of the Overtone library in Clojure using post-tonal music


## Usage

This file is intended to be worked through in a "literate programming"
manner, evaluating one expression at a time.

## Purpose

The ultimate goal is to convert some of the basic utilities here to
work as a generative composition program, along the lines of [an
earlier project](https://github.com/cicerojones/ICMC-generative-music)
of mine in Pure Data


## Examples

Navigate to the post_tonal_overtone directory and start up a 'lein
repl'. Then, in emacs, m-x cider-connect, choosing localhost and tab
completion-ing to get the port that lein started the repl on.

If all went well, now begin evaluating the lisp forms one at a time.
'Use'-ing overtone.live will start up a background process (the
overtone server), which I believe appears as 'coreaudiod' in the
Activity Monitor.

You may want to have a keyboard-macro defined that calls (stop) and
returns to the code you were editing.

## Emacs-users
(Nice superfluous Lisp-y hyphenation there), check out the org file in
the /src/post_tonal_overtone, for more detailed explanation of what's
going on?
