#include <iostream>
#include <math.h>
#include "alien.h"
#include "constant.h"
using namespace std;

Alien::Alien(int x, int y, bool existence, bool hsLasered) {
    Xpos = x;
    Ypos = y;
    beamXpos = x;
    beamYpos = y;
    isExist = existence;
    hasLasered = hsLasered;
}

int Alien::getX() {
    return Xpos;
}

int Alien::getY() {
    return Ypos;
}

int Alien::getLaserX() {
    return beamXpos;
}

int Alien::getLaserY() {
    return beamYpos;
}

bool Alien::getExistence() {
    return isExist;
}

bool Alien::getHasLasered() {
    return hasLasered;
}

void Alien::moveLeft() {
    Xpos -= Db_alienXSpeed;
    if (!hasLasered) {
        beamXpos = Xpos;
    }
}

void Alien::moveRight() {
    Xpos += Db_alienXSpeed;
    if (!hasLasered) {
        beamXpos = Xpos;
    }
}

void Alien::moveDown() {
    Ypos += Db_alienYSpeed;
    if (!hasLasered) {
        beamYpos = Ypos;
    }
}

void Alien::moveLaserDown() {
    beamYpos++;
}

int Alien::getDiffSq(int objX, int objY) {
    return ( (objX - Xpos)*(objX - Xpos) + (objY - Ypos)*(objY - Ypos));
}

void Alien::setLaserX(int newLaserX) {
    beamXpos = newLaserX;
}

void Alien::setLaserY(int newLaserY) {
    beamYpos = newLaserY;
}

void Alien::setExistence(bool newExistence) {
    isExist = newExistence;
}

void Alien::setHasLasered(bool newHasLasered) {
    hasLasered = newHasLasered;
}
