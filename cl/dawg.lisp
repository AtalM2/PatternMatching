; Copyright (C) 2012 by Grégoire Jadi
; See the file LICENSE for copying permission.

(in-package #:exact-string-matching)

;;; This file implements a basic algorithm to build the Directed
;;; Acyclic Word Graph(DAWG) of a given word.

;;; To try it
;;; * (automata-to-graphviz (build-dawg "aabbab") "test.gv")
;;; $ dot -Tpng test.gv -o test.png
;;; $ exo-open test.png

(defvar *state-counter* 0)

(defstruct state
  name primary-edges secondary-edges pointer)

(defun make-state* (&key name primary-edges secondary-edges pointer)
  (make-state :name (or name (incf *state-counter*))
              :primary-edges primary-edges
              :secondary-edges secondary-edges
              :pointer pointer))

(defstruct edge
  label to)

(defun make-edge* (&key label to)
  (make-edge :label label :to to))

(defvar *source* nil)

(defun build-dawg (word)
  (iter
    (initially (setf *state-counter* 0)
               ;; Create a state named *SOURCE*
               (setf *source* (make-state*))
               ;; and let CURRENT-SINK be *SOURCE*.
               (setf current-sink *source*))
    ;; For each letter A of WORD do:
    (for A in-string word)

    ;; Let CURRENT-SINK be UPDATE(CURRENT-SINK A).
    (for current-sink = (update current-sink A)))
  
  ;; Return *SOURCE*.
  *source*)

(defun update (current-sink A)
  (let (
        ;; Create a state named NEW-SINK
        (new-sink (make-state*))

        ;; and let SUFFIX-STATE be undefined.
        (suffix-state nil))
    (iter (initially
           ;; Create a primary edge labeled A from CURRENT-SINK to NEW-SINK
           (push (make-edge* :to new-sink :label A) (state-primary-edges current-sink))

           ;; and let CURRENT-STATE be CURRENT-SINK.
           (setf current-state current-sink))

      ;; While CURRENT-STATE is not *SOURCE* and SUFFIX-STATE is undefined do:
      (while (and (not (eq current-state *source*))
                  (null suffix-state)))

      ;; Let CURRENT-STATE be the state pointed by CURRENT-STATE.
      (for current-state = (state-pointer current-state))

      ;; Check whether CURRENT-STATE has an outgoing edge labeled A.
      (acond
        ;; If CURRENT-STATE has a primary outgoing edge labeled A
        ((find A (state-primary-edges current-state)
               :key #'edge-label)
         ;; then let SUFFIX-STATE be the state to which this edge leads.
         (setf suffix-state (edge-to it)))

        ;; Else if CURRENT-STATE has a secondary outgoing edge labeled A
        ((find A (state-secondary-edges current-state)
               :key #'edge-label)
         ;; then let SUFFIX-STATE be SPLIT(CURRENT-STATE, the secondary outgoing labeled A.
         (setf suffix-state (split current-state it)))

        ;; Else if CURRENT-STATE does not have an outgoing edge labeled A
        (t
         ;; then create a secondary edge from CURRENT-STATE to NEW-SINK labeled A.
         (push (make-edge* :to new-sink :label A) (state-secondary-edges current-state))))

      (finally
       (progn
         ;; If SUFFIX-STATE is still undefined
         (when (null suffix-state)
           ;; let SUFFIX-STATE be *SOURCE*.
           (setf suffix-state *source*))

         ;; Let the suffix pointer of NEW-SINK to point to SUFFIX-STATE
         (setf (state-pointer new-sink) suffix-state)

         ;; and return NEW-SINK.
         (return new-sink))))))

(defun split (parent-state child-edge)
  (let (
        ;; Create a state called NEW-CHILD-STATE.
        (new-child-state (make-state*))
        
        (child-state (edge-to child-edge)))

    ;; Make the secondary edge from PARENT-STATE to CHILD-STATE into a
    ;; primary edge from PARENT-STATE to NEW-CHILD-STATE (with the
    ;; same label)
    (setf (state-secondary-edges parent-state) (delete child-edge (state-secondary-edges parent-state)))
    (setf (edge-to child-edge) new-child-state)
    (push child-edge (state-primary-edges parent-state))

    ;; For every primary and secondary outgoing edge of CHILD-STATE,
    ;; create a secondary outgoing edge of NEW-CHILD-STATE with the
    ;; same label and leading to the same state.
    (iter (for edge in (append (state-primary-edges child-state)
                               (state-secondary-edges child-state)))
      (pushnew edge (state-secondary-edges new-child-state)))

    ;; Set the suffix pointer of NEW-CHILD-STATE equal to that of CHILD-STATE.
    (setf (state-pointer new-child-state) (state-pointer child-state))

    ;; Reset the suffix pointer of CHILD-STATE to point to NEW-CHILD-STATE.
    (setf (state-pointer child-state) new-child-state)

    (iter (initially
           ;; Let CURRENT-STATE be PARENT-STATE.
           (setf current-state parent-state))
      ;; While CURRENT-STATE is not *SOURCE* do:
      (while (not (eq current-state *source*)))

      ;; Let CURRENT-STATE be the state pointed to by the suffix pointer of CURRENT-STATE.
      (for current-state = (state-pointer current-state))

      (aif
       ;; If CURRENT-STATE has a secondary edge to CHILD-STATE,
       (find child-state (state-secondary-edges current-state)
             :key #'edge-to)
       ;; make it a secondary edge to NEW-CHILD-STATE (with the same
       ;; label).
       (setf (edge-to it) new-child-state)

       ;; Else, break out of the while loop.
       (terminate)))
    ;; Return NEW-CHILD-STATE.
    new-child-state))

(defun automata-to-graphviz (automata file-name)
  ;; header
  (with-open-file (*standard-output*
                   file-name
                   :direction :output
                   :if-does-not-exist :create
                   :if-exists :supersede)
    (write-string "
digraph finite_state_machine {
  rankdir=LR;
  size=\"8,5\"
  node [shape = doublecircle];")

    (let ((states (list automata))
          (states-done (list)))
      (do* ((state (car states) (car states)))
           ((null state) 42)
        (setf states (cdr states))
        (push state states-done)

        (iter (for edge in (append (state-primary-edges state)
                                   (state-secondary-edges state)))
          (unless (find (edge-to edge) states-done)
            (pushnew (edge-to edge) states))
          (format t "~&  ~D -> ~D [ label = \"~C\" ];"
                  (state-name state)
                  (state-name (edge-to edge))
                  (edge-label edge)))))
    
    ;; end of graph
    (write-string "
}"))
  t                                     ; REPL sanity
  )
