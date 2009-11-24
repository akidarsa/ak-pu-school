#ifndef _USERINTERFACE_H
#define	_USERINTERFACE_H

#include <QtGui>

class PlayArena;

class UserInterface : public QMainWindow {
    Q_OBJECT
public:
    UserInterface();

private:
    void createMenuBar();

    QMenuBar *bar;
    QMenu *file;
    QAction *reset;
    QAction *clearScore;
    QAction *exit;
    QWidget *main;
    PlayArena *arena;
    QTimer *timer;

private slots:
    void clearHiScore();
    void exitarena();
    void resetarena();
    void start();
    void update();

};

#endif

