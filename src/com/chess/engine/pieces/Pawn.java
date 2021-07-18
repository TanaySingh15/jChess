package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece{
    private final static int[] CANDIDATE_MOVE_COORDINATES={8,16,7,9};
    public Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.PAWN,piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateCoordinateOffset: CANDIDATE_MOVE_COORDINATES){
            final int candidateDestinationCoordinate=this.piecePosition+(candidateCoordinateOffset*this.getPieceAlliance().getDirection());

            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) continue;

            //non attacking pawn move
            if(candidateCoordinateOffset==8&&!board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinate));
            }
            //pawn double jump code
            else if(candidateCoordinateOffset==16&&this.isFirstMove()&&(BoardUtils.SECOND_ROW[this.piecePosition]&&this.getPieceAlliance().isBlack())
            ||(BoardUtils.SECOND_ROW[this.piecePosition]&&this.getPieceAlliance().isBlack()))
            {
                final int behindCandidateDestinationCoordinate=this.piecePosition+(this.pieceAlliance.getDirection()*8);
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied()&&!board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinate));
                }
            }
            //attacking pawn move
            else if(candidateCoordinateOffset==7&&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition]&&this.pieceAlliance.isWhite())||
                    (BoardUtils.FIRST_COLUMN[this.piecePosition]&&this.pieceAlliance.isBlack()))){
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate=board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()){
                        //Adding an attack move
                        //TODO more to do here
                        legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinate));
                    }
                }

            } else if (candidateCoordinateOffset == 9&&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition]&&this.pieceAlliance.isWhite())||
                     (BoardUtils.EIGHTH_COLUMN[this.piecePosition]&&this.pieceAlliance.isBlack()))) {
                //Adding an attack move
                //TODO more to do here
                legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinate));
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }
}
