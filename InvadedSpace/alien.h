#ifndef _ALIEN_H
#define	_ALIEN_H

class Alien {
public:
    Alien(int x = 1, int y = 1, bool existence = true, bool hsLasered = false);

    int getX();
    int getY();
    int getLaserX();
    int getLaserY();
    bool getExistence();
    bool getHasLasered();

    void setLaserX(int newLaserX);
    void setLaserY(int newLaserY);
    void setExistence(bool newExistence);
    void setHasLasered(bool newHasLasered);

    void moveLeft();
    void moveRight();
    void moveDown();
    void moveLaserDown();

    int getDiffSq(int objX, int objY);

private:
    int Xpos;
    int Ypos;

    int beamXpos;
    int beamYpos;

    bool isExist;
    bool hasLasered;
};

#endif
