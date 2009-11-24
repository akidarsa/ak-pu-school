#include <QtGui/QApplication>
#include "userinterface.h"

int main(int argc, char *argv[]) {
    QApplication app(argc, argv);
    UserInterface playGame;
    playGame.show();
    return app.exec();
}
