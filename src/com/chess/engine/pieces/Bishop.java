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

public class Bishop extends Piece{


    private final static int[] CANDIDATE_MOVE_COORDINATES={-9,-7,7,9};
    public Bishop(int piecePosition, Alliance pieceAlliance) {
        super(PieceType.BISHOP,piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateCoordinateOffset: CANDIDATE_MOVE_COORDINATES){
            int candidateDestinationCoordinate=this.piecePosition+candidateCoordinateOffset;
            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                if(isFirstColumnExclusion(this.piecePosition,candidateCoordinateOffset)||
                        isEighthColumnExclusion(this.piecePosition,candidateCoordinateOffset)) break;

                candidateDestinationCoordinate+=candidateCoordinateOffset;
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
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Bishop movePiece(final Move move) {
        return new Bishop(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }


    @Override
    public String toString(){
        return PieceType.BISHOP.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition,final int currentOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition]&&(currentOffset==-9||currentOffset==7);
    }
    private static boolean isEighthColumnExclusion(final int currentPosition,final int currentOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition]&&(currentOffset==-7||currentOffset==9);
    }

}
