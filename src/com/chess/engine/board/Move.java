package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;

import java.util.Objects;

import static com.chess.engine.board.Board.Builder;

public abstract class Move {

    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    public static final Move NULL_MOVE=new NullMove();

    private Move(final Board board,final Piece movedPiece,final int destinationCoordinate){
        this.board=board;
        this.movedPiece=movedPiece;
        this.destinationCoordinate=destinationCoordinate;
    }

    @Override
    public int hashCode(){
        final int prime =31;
        int result=1;
        result=prime*result+this.destinationCoordinate;
        result=prime*result+this.movedPiece.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return destinationCoordinate == move.destinationCoordinate && Objects.equals(board, move.board) && Objects.equals(movedPiece, move.movedPiece);
    }

    public int getCurrentCoordinate(){
        return this.getMovedPiece().getPiecePosition();
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public Piece getMovedPiece(){
        return this.movedPiece;
    }

    public boolean isAttacked(){
        return false;
    }

    public boolean isCastlingMove(){
        return false;
    }

    public Piece getAttackedPiece(){
        return null;
    }

    public Board execute() {

        final Builder builder =new Builder();

        for(final Piece piece : this.board.currentPlayer().getActivePieces()){
            //TODO hashcode and equals for pieces
            if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);

            }
        }

        for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }
        //move the moved piece
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    public static final class MajorMove extends Move{

        public MajorMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

    }

    public static class AttackMove extends Move{
        final Piece attackedPiece;
        public AttackMove(Board board, Piece movedPiece, int destinationCoordinate,final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece=attackedPiece;
        }

        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode()+super.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            final AttackMove that = (AttackMove) o;
            return super.equals(that)&& getAttackedPiece().equals(that.getAttackedPiece());
        }

        @Override
        public Board execute() {
            return null;
        }

        @Override
        public boolean isAttacked() {
            return true;
        }

        @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }
    }

    public static final class PawnMove extends Move{

        public PawnMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

    }

    public static class PawnAttackMove extends AttackMove{

        public PawnAttackMove(Board board, Piece movedPiece, int destinationCoordinate,final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate,attackedPiece);
        }

    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove{

        public PawnEnPassantAttackMove(Board board, Piece movedPiece, int destinationCoordinate,final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate,attackedPiece);
        }

    }

    public static final class PawnJump extends Move{

        public PawnJump(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            final Builder builder=new Builder();

            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }

            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    static abstract class CastleMove extends Move{

        public CastleMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class KingSideCastleMove extends CastleMove{

        public KingSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class QueenSideCastleMove extends CastleMove{

        public QueenSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class NullMove extends Move{

        public NullMove() {
            super(null, null, -1);
        }

        public Board execute(){
            throw new RuntimeException("cannot execute the null move");
        }
    }

    public static class MoveFactory{
        private MoveFactory(){
            throw new RuntimeException("Not Instantiable");
        }

        public static Move createMove(final Board board,final int currentCoordinate,final int destinationCoordinate){

            for(final Move move:board.getAllLegalMoves()){
                if(move.getCurrentCoordinate()==currentCoordinate && move.getDestinationCoordinate()==destinationCoordinate)
                    return move;
            }
            return NULL_MOVE;
        }
    }


}
