package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    private final int cachedHashCode;
    Piece(final PieceType pieceType,final int piecePosition,final Alliance pieceAlliance){
        this.pieceAlliance=pieceAlliance;
        this.piecePosition=piecePosition;
        this.pieceType=pieceType;
        this.cachedHashCode=computeHashCode();
        this.isFirstMove=false;
    }

    private int computeHashCode(){
        int result=pieceType.hashCode();
        result=31*result+pieceAlliance.hashCode();
        result=31*result+piecePosition;
        result=31*result+(isFirstMove?1:0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return piecePosition == piece.piecePosition && isFirstMove == piece.isFirstMove && pieceType == piece.pieceType && pieceAlliance == piece.pieceAlliance;
    }

    @Override
    public int hashCode(){
        return this.cachedHashCode;
    }

    public int getPiecePosition(){
        return this.piecePosition;
    }
    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }
    public boolean isFirstMove(){
        return this.isFirstMove;
    }
    public PieceType getPieceType(){
        return pieceType;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);

    public enum PieceType{
        PAWN("P"){
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }
        };

        private String pieceName;
        PieceType(final String pieceName)
        {
            this.pieceName=pieceName;

        }

        @Override
        public String toString(){
            return this.pieceName;
        }

        public abstract boolean isKing();


    }
}
