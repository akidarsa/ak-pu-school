#include <QtGui>
#include <iostream>
#include "playarena.h"
#include "constant.h"
#include <fstream>
#include <string>
using namespace std;

PlayArena::PlayArena(QWidget *parent) : QWidget(parent) {
    setFocusPolicy(Qt::StrongFocus);
    setMinimumSize(Db_arenaWidth, Db_arenaHeight);
    setMaximumSize(Db_arenaWidth, Db_arenaHeight);
    setMouseTracking(true);

    resetGame();
}

void PlayArena::resetGame() {
    isRunning = false;
    isGameOver = false;
    isWon = false;
    buffer = false;

    cannonX = Db_arenaWidth / 2;
    cannonBulletX = Db_arenaWidth / 2;
    cannonBulletY = Db_cannonY;
    isShot = false;
    cannonLife = 3;
    cannonColor = Db_cannonColor3;

    alienCounter = Db_alienMaxNumber;
    isAlienMovingRight = true;
    moveDownSmoother = 0;
    alienSlower = 0;

    score = 0;

    Alien tempAlien;
    vector<Alien>::iterator iter;
    aliens.resize(Db_alienMaxNumber);
    iter = aliens.begin();
    for (int i = 0; i < Db_alienMaxNumber; i++, iter++) {
        tempAlien = Alien(Db_alienStartX + (i % Db_aliensPerRows) * Db_aliensGap,
                Db_alienStartY + (i / Db_aliensPerRows) * Db_aliensGap,
                true, false);
        *iter = tempAlien;
    }

    // Initialize the hiScore
    try {
        char buffer[256];
        ifstream myfile(".score");
        myfile.getline(buffer, 10);
        hiScore = atoi(buffer);
        myfile.close();
    } catch (...) {
        hiScore = 0;
    }
}

void PlayArena::cannonDamaged() {
    cannonLife--;
    if (cannonLife >= 3) {
        cannonColor = Db_cannonColor3;
    } else if (cannonLife == 2) {
        cannonColor = Db_cannonColor2;
    } else if (cannonLife == 1) {
        cannonColor = Db_cannonColor1;
    } else {
        isGameOver = true;
    }
}

void PlayArena::paintEvent(QPaintEvent *event) {
    event = event;
    int ww = width(); // widget's width
    int wh = height(); // widget's height

    updatePosition();

    // draw background
    QPainter painter(this);
    painter.setBrush(Db_backgroundColor);
    painter.drawRect(0, 0, ww, wh);


    // Draw the score
    painter.setPen(Db_textColor);
    string tempStr = "Score: ";
    tempStr += itoa(score, 10);
    char * scoreText;
    scoreText = new char[tempStr.length() + 1];
    strcpy(scoreText, tempStr.c_str());
    painter.drawText(Db_scoreX, Db_scoreY, tr(scoreText));

    // Draw the high score
    string tempStr2 = "HiScore: ";
    tempStr2 += itoa(hiScore, 10);
    char * scoreText2;
    scoreText2 = new char[tempStr2.length() + 1];
    strcpy(scoreText2, tempStr2.c_str());
    painter.drawText(Db_hiScoreX, Db_hiScoreY, tr(scoreText2));


    // Draw cannon
    painter.setBrush(cannonColor);
    painter.drawRect(cannonX - Db_cannonRadius,
            Db_cannonY - Db_cannonRadius,
            Db_cannonDiameter,
            Db_cannonDiameter);

    // Draw cannon bullet
    painter.setPen(Db_bulletColor);
    painter.drawLine(cannonBulletX, cannonBulletY, cannonBulletX, cannonBulletY + Db_bulletLength);

    // Draw aliens
    painter.setBrush(Db_alienColor);
    vector<Alien>::iterator iter;
    iter = aliens.begin();
    for (int i = 0; i < Db_alienMaxNumber; i++, iter++) {
        if ((*iter).getExistence())
            painter.drawEllipse((*iter).getX() - Db_alienRadius,
                (*iter).getY() - Db_alienRadius,
                Db_alienDiameter,
                Db_alienDiameter);
    }

    // Draw beams
    vector<Alien>::iterator ita;
    ita = aliens.begin();
    painter.setPen(Db_beamColor);
    for (int i = 0; i < Db_alienMaxNumber; i++, ita++) {
        if ((*ita).getExistence())
            painter.drawLine((*ita).getLaserX(), (*ita).getLaserY(), (*ita).getLaserX(), (*ita).getLaserY() - Db_bulletLength);
    }


    if (buffer) {
        painter.setPen(Db_textColor);
        painter.drawText(Db_arenaWidth * 2 / 5, Db_arenaHeight / 2 + 20, tr("More Alien Attacking!!!"));
    }



    if (isRunning && !isGameOver && !isWon) {
        // Draw cannon life indicators
        if (cannonLife >= 3) {
            painter.setBrush(Db_cannonColor3);
            painter.drawRect(Db_cannonRadius * 2,
                    Db_arenaHeight - Db_cannonRadius * 2,
                    Db_cannonDiameter,
                    Db_cannonDiameter);
        }

        if (cannonLife >= 2) {
            painter.setBrush(Db_cannonColor2);
            painter.drawRect(Db_cannonRadius,
                    Db_arenaHeight - Db_cannonRadius * 2,
                    Db_cannonDiameter,
                    Db_cannonDiameter);
        }

        if (cannonLife >= 1) {
            painter.setBrush(Db_cannonColor1);
            painter.drawRect(0,
                    Db_arenaHeight - Db_cannonRadius * 2,
                    Db_cannonDiameter,
                    Db_cannonDiameter);
        }


    } else if (isGameOver) {
        // Draw Game Over message
        painter.setPen(Db_textColor);
        painter.drawText(30, Db_arenaHeight / 2, tr("(>_<) The world as you know it doesn't exist anymore"));
    } else if (isWon) {
        // Draw Congratulation message
        painter.setPen(Db_textColor);
        painter.drawText(Db_arenaWidth * 2 / 5 - 30, Db_arenaHeight / 2, tr("Congratulations! \\(^_^)/"));
    } else {
        // Draw start message
        painter.setPen(Db_textColor);
        painter.drawText(Db_arenaWidth * 2 / 5 - 30, Db_arenaHeight / 2, tr("Let's do This!!! Click or Space!"));
    }




}

void PlayArena::keyPressEvent(QKeyEvent * event) {
    isRunning = true;

    if (isGameOver || isWon) {
        if (buffer) resetGame();
        else buffer = true;
    }

    switch (event -> key()) {
        case Qt::Key_Left :
            if (cannonX > Db_moveAreaMinX) cannonX--;
            break;
        case Qt::Key_Right :
            if (cannonX < Db_moveAreaMaxX) cannonX++;
            break;
        case Qt::Key_Space :
                    isShot = true;
            break;
        default:
            return;
    }

}

void PlayArena::mouseMoveEvent(QMouseEvent * event) {
    static int lastX = event -> x();
    int newX = event -> x();
    while ((lastX > newX) && (cannonX > Db_moveAreaMinX)) {
        cannonX--;
        lastX--;
    }
    while ((lastX < newX) && (cannonX < Db_moveAreaMaxX)) {
        cannonX++;
        lastX++;
    }
    lastX = newX;
}

void PlayArena::mousePressEvent(QMouseEvent * event) {
    event = event;
    if (isGameOver || isWon) {
        if (buffer) resetGame();
        else buffer = true;
    }
    isRunning = true;
    isShot = true;
}

void PlayArena::clearHiScore() {
    newHiScoreTxt = (itoa(0, 10)).c_str();
    ofstream ofile(".score");
    ofile.write(newHiScoreTxt, 10);
    hiScore = 0;
    ofile.close();
}

void PlayArena::updateHiScore() {
    if (score > hiScore) {
        newHiScoreTxt = (itoa(score, 10)).c_str();
        ofstream ofile(".score");
        ofile.write(newHiScoreTxt, 10);
        hiScore = score;
        ofile.close();
    }
}

void PlayArena::updatePosition() {
    if (isRunning && !isWon && !isGameOver) {
        iter1 = aliens.begin();
        iter2 = iter1 + (Db_alienMaxNumber - 1);

        // Update the beam
        if (isShot) {
            cannonBulletY -= 3;

            if (cannonBulletY <= 0) isShot = false;

            for (int i = 0; i < Db_alienMaxNumber; i++, iter1++) {
                if ((*iter1).getDiffSq(cannonBulletX, cannonBulletY) <= Db_alienRadius * Db_alienRadius && (*iter1).getExistence()) {
                    isShot = false;
                    (*iter1).setExistence(false);
                    score += Db_scoreInc;
                    if (--alienCounter <= 0) {
                        isWon = true;
                    }
                }
            }

        } else {
            cannonBulletX = cannonX;
            cannonBulletY = Db_cannonY;
        }

        // Update aliens
        iter1 = aliens.begin();
        if ((*iter1).getX() >= Db_moveAreaMinX && (*iter2).getX() <= Db_moveAreaMaxX) {

            if (--alienSlower <= 0) {
                if (isAlienMovingRight) {
                    for (int i = 0; i < Db_alienMaxNumber; i++, iter1++)
                        (*iter1).moveRight();
                } else {
                    for (int i = 0; i < Db_alienMaxNumber; i++, iter1++)
                        (*iter1).moveLeft();
                }
                iter1 = aliens.begin();
                alienSlower = (Db_cannonY - (*iter1).getY()) / (2 * Db_aliensGap);

            }

        } else {

            for (int i = 0; i < Db_alienMaxNumber; i++, iter1++)
                (*iter1).moveDown();

            if (++moveDownSmoother >= 10) {
                isAlienMovingRight = !isAlienMovingRight;
                moveDownSmoother = 0;
                iter1 = aliens.begin();
                if (isAlienMovingRight) {
                    for (int i = 0; i < Db_alienMaxNumber; i++, iter1++)
                        (*iter1).moveRight();
                } else {
                    for (int i = 0; i < Db_alienMaxNumber; i++, iter1++)
                        (*iter1).moveLeft();
                }
            }

        }


        // check for game over
        iter1 = aliens.begin();
        iter2 = iter1 + (Db_alienMaxNumber - 1);
        if ((*iter2).getY() >= Db_cannonY) {
            for (int i = 0; i < Db_alienMaxNumber; i++, iter1++) {
                if ((*iter1).getExistence() && (*iter1).getY() >= Db_cannonY)
                    isGameOver = true;
            }
        }

        // Set random shoot
        iter1 = aliens.begin();

        iter1 += rand() % Db_alienMaxNumber;
        if (rand() % Db_beamRandSeed == 0) {
            (*iter1).setHasLasered(true);
        }

        // Keep the beam moving and cannon life
        iter1 = aliens.begin();
        for (int i = 0; i < Db_alienMaxNumber; i++, iter1++) {
            if ((*iter1).getExistence() && (*iter1).getHasLasered()) {
                (*iter1).moveLaserDown();
                Alien cannon = Alien(cannonX, Db_cannonY, true, false);
                if (cannon.getDiffSq((*iter1).getLaserX(), (*iter1).getLaserY()) <= Db_cannonRadius * Db_cannonRadius) {
                    (*iter1).setHasLasered(false);
                    (*iter1).setLaserX((*iter1).getX());
                    (*iter1).setLaserY((*iter1).getY());
                    cannonDamaged();
                }

                if ((*iter1).getLaserY() >= Db_arenaHeight) {
                    (*iter1).setHasLasered(false);
                    (*iter1).setLaserX((*iter1).getX());
                    (*iter1).setLaserY((*iter1).getY());
                }

            }
        }


    }
}





