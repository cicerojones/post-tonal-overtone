# tutorial

Exploration of the Overtone library in Clojure using post-tonal music


## Usage

The files in
[src/org-files](https://github.com/cicerojones/post-tonal-overtone/tree/master/src/post_tonal_overtone/org_files)
are intended to be worked through in a "literate programming" manner,
evaluating one expression at a time. Additionally. since Github
renders .org files, they can be read on the github website as a less
interactive kind of tutorial.

## Purpose

The ultimate goal is to convert some of the basic utilities here to
work as a generative composition program, along the lines of [an
earlier project](https://github.com/cicerojones/ICMC-generative-music)
of mine in Pure Data


## Examples

Navigate to the post_tonal_overtone directory and start up a 'lein
repl'. Then, in emacs, m-x cider-connect, choosing localhost and tab
completion-ing to get the port that lein started the repl on.

If all went well, now begin evaluating the lisp forms [in the org-mode
src blocks? or in some dedicated .clj file?] one at a time.

'Use'-ing overtone.live will start up a background process (the
overtone server), which I believe appears as 'coreaudiod' in the
Activity Monitor.

You may want to have a keyboard-macro defined that calls (stop) and
returns to the code you were editing.

### Note
Why does ```lein repl``` fail with a message about namespace problems
in project.clj?

Additionally, be mindful of what your audio output device is, when it
comes time to use overtone. For example, I have a list of several
'Devices' found when loading the SC_Audiodriver. For some reason,
however, I had problems trying to use the Soundflower interface.
Figure out why.

## Emacs-users
(Nice superfluous Lisp-y hyphenation there), check out the org file in
the /src/post_tonal_overtone, for more detailed explanation of what's
going on?

Begin to create .org files that combine overtone babel samples, with a
kind of little schemer approach, that is, with nodes that hide answers
to questions about making overtone things work.
