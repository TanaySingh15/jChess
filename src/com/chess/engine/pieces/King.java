package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Move.AttackMove;
import com.google.common.collect.ImmutableList;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends Piece{
    private final static int[] CANDIDATE_MOVE_COORDINATES={-9,-8,-7,-1,1,7,8,9};

    public King(final int piecePosition,final Alliance pieceAlliance) {
        super(PieceType.KING,piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves=new ArrayList<>();
        for (final int candidateCoordinateOffset: CANDIDATE_MOVE_COORDINATES) {
            final int candidateDestinationCoordinate = this.piecePosition + candidateCoordinateOffset;

            if(isFirstColumnExclusion(this.piecePosition,candidateCoordinateOffset)||
                isEighthColumnExclusion(this.piecePosition,candidateCoordinateOffset)){
                continue;
            }

            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){

                final Tile candidateDestinationTile= board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                }
                else
                {
                    final Piece pieceAtDestination=candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance=pieceAtDestination.getPieceAlliance();
                    if(this.pieceAlliance!=pieceAlliance){
                        legalMoves.add(new AttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                    }
                }
            }
        }
            return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public King movePiece(final Move move) {
        return new King(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString(){
        return PieceType.KING.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition,final int currentOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition]&&(currentOffset==-9||currentOffset==-1||currentOffset==7);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition,final int currentOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition]&&(currentOffset==-7||currentOffset==1||currentOffset==9);
    }
}
