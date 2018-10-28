
%%%%%%%%% Simple Prolog Planner %%%%%%%%%%%%%%%%%%%%%%%%%%
%%%
%%% Based on one of the sample programs in:
%%%
%%% Artificial Intelligence:
%%% Structures and strategies for complex problem solving
%%%
%%% by George F. Luger and William A. Stubblefield
%%%
%%% Modified by Tyler Harbin-Giuntoli for CAP4630
%%% at the University of Central Florida
%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
:- module( planner,
	   [
	       plan/4,change_state/3,conditions_met/2,member_state/2,
	       move/3,go/2,test/0,test2/0
	   ]).

:- [utils].

plan(State, Goal, _, Moves) :-	equal_set(State, Goal),
				write('moves are'), nl,
				reverse_print_stack(Moves).
plan(State, Goal, Been_list, Moves) :-
				move(Name, Preconditions, Actions),
				conditions_met(Preconditions, State),
				change_state(State, Actions, Child_state),
				not(member_state(Child_state, Been_list)),
				stack(Child_state, Been_list, New_been_list),
				stack(Name, Moves, New_moves),
			plan(Child_state, Goal, New_been_list, New_moves),!.

change_state(S, [], S).
change_state(S, [add(P)|T], S_new) :-	change_state(S, T, S2),
					add_to_set(P, S2, S_new), !.
change_state(S, [del(P)|T], S_new) :-	change_state(S, T, S2),
					remove_from_set(P, S2, S_new), !.
conditions_met(P, S) :- subset(P, S).

member_state(S, [H|_]) :-	equal_set(S, H).
member_state(S, [_|T]) :-	member_state(S, T).

/* move types */
/* Room 1 Move */
move(pickup(X), [handempty, clear(X), on(X, Y), ontable1(Y), inroom1],
		[del(handempty), del(clear(X)), del(on(X, Y)),
				 add(clear(Y)),	add(holding(X))]).
/* Room 2 Move */
move(pickup(X), [handempty, clear(X), on(X, Y), ontable2(Y), inroom2],
		[del(handempty), del(clear(X)), del(on(X, Y)),
				 add(clear(Y)),	add(holding(X))]).


/* Room 1 Move */
move(pickup(X), [handempty, clear(X), ontable1(X), inroom1],
		[del(handempty), del(clear(X)), del(ontable1(X)),
				 add(holding(X))]).
/* Room 2 Move */
move(pickup(X), [handempty, clear(X), ontable2(X), inroom2],
		[del(handempty), del(clear(X)), del(ontable2(X)),
				 add(holding(X))]).


/* Room 1 Move */
move(putdown(X), [holding(X), inroom1],
		[del(holding(X)), add(ontable1(X)), add(clear(X)),
				  add(handempty)]).
/* Room 2 Move */
move(putdown(X), [holding(X), inroom2],
		[del(holding(X)), add(ontable2(X)), add(clear(X)),
				  add(handempty)]).


/* Room 1 Move */
move(stack(X, Y), [holding(X), clear(Y), ontable1(Y), inroom1],
		[del(holding(X)), del(clear(Y)), add(handempty), add(on(X, Y)),
				add(clear(X))]).
move(stack(X, Y), [holding(X), clear(Y), ontable1(Z), on(Y,Z), inroom1],
		[del(holding(X)), del(clear(Y)), add(handempty), add(on(X, Y)),
				add(clear(X))]).
move(stack(X, Y), [holding(X), clear(Y), ontable1(A), on(Z,A), on(Y,Z), inroom1],
		[del(holding(X)), del(clear(Y)), add(handempty), add(on(X, Y)),
				add(clear(X))]).
move(stack(X, Y), [holding(X), clear(Y), ontable1(B), on(A,B), on(Z,A), on(Y,Z), inroom1],
		[del(holding(X)), del(clear(Y)), add(handempty), add(on(X, Y)),
				add(clear(X))]).
/* Room 2 Move */
move(stack(X, Y), [holding(X), clear(Y), ontable2(Y), inroom2],
		[del(holding(X)), del(clear(Y)), add(handempty), add(on(X, Y)),
				add(clear(X))]).
move(stack(X, Y), [holding(X), clear(Y), ontable2(Z), on(Y,Z), inroom2],
		[del(holding(X)), del(clear(Y)), add(handempty), add(on(X, Y)),
				add(clear(X))]).
move(stack(X, Y), [holding(X), clear(Y), ontable2(A), on(Z,A), on(Y,Z), inroom2],
		[del(holding(X)), del(clear(Y)), add(handempty), add(on(X, Y)),
				add(clear(X))]).
move(stack(X, Y), [holding(X), clear(Y), ontable2(B), on(A,B), on(Z,A), on(Y,Z), inroom2],
        [del(holding(X)), del(clear(Y)), add(handempty), add(on(X, Y)),
                add(clear(X))]).
/* Room Changing Moves */
move(goroom1, [inroom2], [del(inroom2), add(inroom1)]).

move(goroom2, [inroom1], [del(inroom1), add(inroom2)]).

/* run commands */

go(S, G) :- plan(S, G, [S], []).

test :- go([inroom1, handempty, ontable1(b), ontable1(c), on(a, b), clear(c), clear(a)],
	          [inroom1, handempty, ontable1(c), on(a,b), on(b, c), clear(a)]).

test2 :- go([inroom1, handempty, ontable1(b), ontable1(c), on(a, b), clear(c), clear(a)],
	          [inroom2, handempty, ontable1(a), ontable2(b), on(c, b), clear(a), clear(c)]).


