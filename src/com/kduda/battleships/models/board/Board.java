package com.kduda.battleships.models.board;

import com.kduda.battleships.models.units.GroundLevelUnit;
import com.kduda.battleships.models.units.Plane;
import com.kduda.battleships.models.units.Unit;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Board extends Parent {
    public int units = 19;
    private VBox column = new VBox();
    private boolean isEnemyBoard = false;

    public Board(boolean isEnemyBoard, EventHandler<? super MouseEvent> mouseClickHandler,
                 EventHandler<? super MouseEvent> mouseEnteredHandler,
                 EventHandler<? super MouseEvent> mouseExitedHandler) {
        this.isEnemyBoard = isEnemyBoard;
        for (int y = 0; y < 22; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 14; x++) {
                Cell cell = new Cell(x, y, this);
                cell.setOnMouseClicked(mouseClickHandler);
                cell.setOnMouseEntered(mouseEnteredHandler);
                cell.setOnMouseExited(mouseExitedHandler);
                row.getChildren().add(cell);
            }
            column.getChildren().add(row);
        }
        getChildren().add(column);
    }

    public boolean placeUnit(Unit unit, Position cellPosition) {
        boolean wasUnitPlaced;

        if (unit instanceof GroundLevelUnit)
            wasUnitPlaced = placeGroundLevelUnit((GroundLevelUnit) unit, cellPosition);
        else
            wasUnitPlaced = placePlane((Plane) unit, cellPosition);

        return wasUnitPlaced;
    }

    private boolean placeGroundLevelUnit(GroundLevelUnit unit, Position cellPosition) {
        if (unit.getOrientation() == Orientation.VERTICAL) {
            if (isVerticalLocationValid(unit, cellPosition)) placeVerticalUnit(unit, cellPosition);
            else return false;

        } else {
            if (isHorizontalLocationValid(unit, cellPosition)) placeHorizontalUnit(unit, cellPosition);
            else return false;
        }
        return true;
    }

    private boolean placePlane(Plane plane, Position cellPosition) {
        switch (plane.getDirection()) {
            case North:
                if (isNorthLocationValid(plane, cellPosition)) placePlaneNorth(plane, cellPosition);
                else return false;
                break;
            case East:
                if (isEastLocationValid(plane, cellPosition)) placePlaneEast(plane, cellPosition);
                else return false;
                break;
            case South:
                if (isSouthLocationValid(plane, cellPosition)) placePlaneSouth(plane, cellPosition);
                else return false;
                break;
            case West:
                if (isWestLocationValid(plane, cellPosition)) placePlaneWest(plane, cellPosition);
                else return false;
                break;
        }
        return true;
    }


    private boolean isNorthLocationValid(Plane plane, Position cellPosition) {
        int xPosition = cellPosition.getX();
        int yPosition = cellPosition.getY();
        int length = plane.LENGTH;

        if (!isVerticalLocationPlaneValid(xPosition, yPosition, length)) return false;
        //noinspection RedundantIfStatement
        if (!areHorizontalNeighborsValid(xPosition, yPosition)) return false;

        return true;
    }

    private boolean isEastLocationValid(Plane plane, Position cellPosition) {
        return true;
        //TODO: implement
    }

    private boolean isSouthLocationValid(Plane plane, Position cellPosition) {
        return true;
        //TODO: implement
    }

    private boolean isWestLocationValid(Plane plane, Position cellPosition) {
        return true;
        //TODO: implement
    }

    private boolean isVerticalLocationPlaneValid(int xPosition, int yPosition, int length) {
        for (int i = yPosition; i > yPosition - length; i--) {
            if (!isValidPoint(xPosition, i)) return false;

            Cell cell = getCell(xPosition, i);

            if (!cell.isEmpty()) return false;

            for (Cell neighbor : getAdjacentCells(xPosition, i))
                if (!neighbor.isEmpty()) return false;
        }
        return true;
    }

    private boolean areHorizontalNeighborsValid(int xPosition, int yPosition) {
        if (!isValidPlacementCell(xPosition - 1, yPosition)) return false;
        //noinspection RedundantIfStatement
        if (!isValidPlacementCell(xPosition + 1, yPosition)) return false;

        return true;
    }

    private boolean isValidPlacementCell(int xPosition, int yPosition) {
        if (!isValidPoint(xPosition, yPosition)) return false;

        Cell cell = getCell(xPosition, yPosition);

        if (!cell.isEmpty()) return false;

        for (Cell neighbor : getAdjacentCells(xPosition, yPosition))
            if (!neighbor.isEmpty()) return false;

        return true;
    }


    private void placePlaneNorth(Plane plane, Position cellPosition) {
        //TODO: implement
    }

    private void placePlaneEast(Plane plane, Position cellPosition) {
        //TODO: implement
    }

    private void placePlaneSouth(Plane plane, Position cellPosition) {
        //TODO: implement
    }

    private void placePlaneWest(Plane plane, Position cellPosition) {
        //TODO: implement
    }

    private void placeVerticalUnit(GroundLevelUnit unit, Position cellPosition) {
        int unitLength = unit.LENGTH;
        int xPosition = cellPosition.getX();
        int yPosition = cellPosition.getY();

        for (int i = yPosition; i < yPosition + unitLength; i++) {
            Cell cell = getCell(xPosition, i);
            cell.setUnit(unit);
            if (!this.isEnemyBoard) {
                cell.setColors(Color.WHITE, Color.GREEN);
                cell.saveCurrentColors();
            }
        }
    }

    private void placeHorizontalUnit(GroundLevelUnit unit, Position cellPosition) {
        int unitLength = unit.LENGTH;
        int xPosition = cellPosition.getX();
        int yPosition = cellPosition.getY();

        for (int i = xPosition; i < xPosition + unitLength; i++) {
            Cell cell = getCell(i, yPosition);
            cell.setUnit(unit);
            cell.saveCurrentColors();
            if (!this.isEnemyBoard) {
                cell.setColors(Color.WHITE, Color.GREEN);
                cell.saveCurrentColors();
            }
        }
    }


    private boolean isVerticalLocationValid(GroundLevelUnit unit, Position cellPosition) {
        int unitLength = unit.LENGTH;
        int xPosition = cellPosition.getX();
        int yPosition = cellPosition.getY();

        for (int i = yPosition; i < yPosition + unitLength; i++) {
            if (!isValidPoint(xPosition, i)) return false;

            Cell cell = getCell(xPosition, i);

            if (!cell.isSurfaceValid(unit)) return false;

            if (!cell.isEmpty()) return false;

            for (Cell neighbor : getAdjacentCells(xPosition, i))
                if (!neighbor.isEmpty()) return false;
        }
        return true;
    }

    private boolean isHorizontalLocationValid(GroundLevelUnit unit, Position cellPosition) {
        int unitLength = unit.LENGTH;
        int xPosition = cellPosition.getX();
        int yPosition = cellPosition.getY();

        for (int i = xPosition; i < xPosition + unitLength; i++) {
            if (!isValidPoint(i, yPosition)) return false;

            Cell cell = getCell(i, yPosition);

            if (!cell.isSurfaceValid(unit)) return false;

            if (!cell.isEmpty()) return false;

            for (Cell neighbor : getAdjacentCells(i, yPosition))
                if (!neighbor.isEmpty()) return false;
        }
        return true;
    }


    private boolean isValidPoint(int x, int y) {
        return x >= 0 && x < 14 && y >= 0 && y < 22;
    }

    public Cell getCell(int x, int y) {
        HBox row = (HBox) column.getChildren().get(y);
        return (Cell) row.getChildren().get(x);
    }

    private Cell[] getAdjacentCells(int x, int y) {
        Position[] positions = new Position[]{
                new Position(x, y - 1), //N
                new Position(x + 1, y - 1), //NE
                new Position(x + 1, y), //E
                new Position(x + 1, y + 1), //SE
                new Position(x, y + 1), //S
                new Position(x - 1, y + 1), //SW
                new Position(x - 1, y), //W
                new Position(x - 1, y - 1) //NW
        };

        List<Cell> neighbors = new ArrayList<>();

        for (Position position : positions) {
            int xPosition = position.getX();
            int yPosition = position.getY();
            if (isValidPoint(xPosition, yPosition))
                neighbors.add(getCell(xPosition, yPosition));
        }
        //noinspection ToArrayCallWithZeroLengthArrayArgument
        return neighbors.toArray(new Cell[0]);
    }

    public void showPlacementHint(Unit unit, Cell cell) {
        Position cellPosition = new Position(cell.POSITION.getX(), cell.POSITION.getY());

        if (unit instanceof GroundLevelUnit) {
            GroundLevelUnit currentUnit = (GroundLevelUnit) unit;

            if (currentUnit.getOrientation() == Orientation.VERTICAL) {
                if (isVerticalLocationValid(currentUnit, cellPosition)) {
                    changeColorsVertical(cell, currentUnit.LENGTH, Color.GREEN, Color.GREEN);
                } else {
                    changeColorsVertical(cell, currentUnit.LENGTH, Color.RED, Color.RED);
                }

            } else {
                if (isHorizontalLocationValid(currentUnit, cellPosition)) {
                    changeColorsHorizontal(cell, currentUnit.LENGTH, Color.GREEN, Color.GREEN);
                } else {
                    changeColorsHorizontal(cell, currentUnit.LENGTH, Color.RED, Color.RED);
                }
            }
        } else {
            //TODO: hint dla samolotu
            return;
        }
    }

    private void changeColorsVertical(Cell cell, int length, Color fillColor, Color strokeColor) {
        int xPosition = cell.POSITION.getX();
        int yPosition = cell.POSITION.getY();

        for (int i = yPosition; i < yPosition + length; i++) {
            Cell currCell = getCell(xPosition, i);
            currCell.saveCurrentColors();
            currCell.setColors(fillColor, strokeColor);
        }
    }

    private void changeColorsHorizontal(Cell cell, int length, Color fillColor, Color strokeColor) {
        int xPosition = cell.POSITION.getX();
        int yPosition = cell.POSITION.getY();

        for (int i = xPosition; i < xPosition + length; i++) {
            Cell currCell = getCell(i, yPosition);
            currCell.saveCurrentColors();
            currCell.setColors(fillColor, strokeColor);
        }
    }

    public void removePlacementHint(Unit unit, Cell cell) {
        if (unit instanceof GroundLevelUnit) {
            GroundLevelUnit currentUnit = (GroundLevelUnit) unit;
            if (currentUnit.getOrientation() == Orientation.VERTICAL) {
                restoreColorsVertical(cell, currentUnit.LENGTH);
            } else {
                restoreColorsHorizontal(cell, currentUnit.LENGTH);
            }
        } else {
            //TODO: restore dla samolotow
            return;
        }
    }

    private void restoreColorsVertical(Cell cell, int length) {
        int xPosition = cell.POSITION.getX();
        int yPosition = cell.POSITION.getY();

        for (int i = yPosition; i < yPosition + length; i++) {
            Cell currCell = getCell(xPosition, i);
            currCell.loadSavedColors();
        }
    }

    private void restoreColorsHorizontal(Cell cell, int length) {
        int xPosition = cell.POSITION.getX();
        int yPosition = cell.POSITION.getY();

        for (int i = xPosition; i < xPosition + length; i++) {
            Cell currCell = getCell(i, yPosition);
            currCell.loadSavedColors();
        }
    }
}