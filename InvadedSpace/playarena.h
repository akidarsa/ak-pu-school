#ifndef _PLAYARENA_H
#define	_PLAYARENA_H

#include <algorithm>
#include <QtGui>
#include "alien.h"
#include <vector>
#include <iostream>
#include <fstream>
#include <math.h>

#define ABS(X) ((X) > 0 ? (X) : (-(X)))

using namespace std;

class PlayArena : public QWidget {
public:
    PlayArena(QWidget *parent = 0);
    void resetGame();
    void clearHiScore();
    void updateHiScore();

private:

    vector<Alien>::iterator iter1;
    vector<Alien>::iterator iter2;

    void paintEvent(QPaintEvent *event);
    void keyPressEvent(QKeyEvent * event);
    void mousePressEvent(QMouseEvent * event);
    void mouseMoveEvent(QMouseEvent * event);

    bool isRunning;
    bool isGameOver;
    bool isWon;
    bool buffer;

    int cannonX;
    int cannonBulletX;
    int cannonBulletY;
    bool isShot;
    int cannonLife;
    QColor cannonColor;

    vector<Alien> aliens;
    int alienCounter;
    bool isAlienMovingRight;
    int moveDownSmoother;
    int alienSlower;

    int score;
    int hiScore;

    const char * newHiScoreTxt;

    void updatePosition();
    void cannonDamaged();

    /**
     * C++ version 0.4 std::string style "itoa":
     * Contributions from Stuart Lowe, Ray-Yuan Sheu,
     * Rodrigo de Salvo Braz, Luc Gallant, John Maloney
     * and Brian Hunt
     */
    std::string itoa(int value, int base) {

        std::string buf;

        // check that the base if valid
        if (base < 2 || base > 16) return buf;

        enum {
            kMaxDigits = 35
        };
        buf.reserve(kMaxDigits); // Pre-allocate enough space.

        int quotient = value;

        // Translating number to string with base:
        do {
            buf += "0123456789abcdef"[ ABS(quotient % base) ];
            quotient /= base;
        } while (quotient);

        // Append the negative sign
        if (value < 0) buf += '-';

        reverse(buf.begin(), buf.end());
        return buf;
    }
};


#endif

