import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.io.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.*;


public class Minesweeper extends JFrame {

    /*
     * Pedro Perone
     * 999992958
     * MCIS5103_029192S: Advanced Programming Concepts
     */
    public static class Globals {
      public static int cols;
      public static int rows;
      public static int width;
      public static int height;
      public static int mines;
    }

    private static final long serialVersionUID = 1L;
    private JLabel statusbar;

    private class Board extends JPanel {

      private static final long serialVersionUID = 1L;
    
      private int[] field;
      private boolean inGame;
      private int minesLeft;
      private Image[] img;
    
      private int allCells;
      private final JLabel statusbar;
    
      public Board(JLabel statusbar) {
        this.statusbar = statusbar;
        initBoard();
      }
    
      private void initBoard() {
        setPreferredSize(new Dimension(Minesweeper.Globals.width, Minesweeper.Globals.height));
  
        img = new Image[13];
  
        for (int i = 0; i < 13; i++) {
          String path = "img/" + i + ".png";
          img[i] = (new ImageIcon(path)).getImage();
        }
  
        addMouseListener(new MinesAdapter());
        newGame();
      }

      private void newGame() {
        int cell;
        int mines = Minesweeper.Globals.mines;
        int cols = Minesweeper.Globals.cols;
        int rows = Minesweeper.Globals.rows;
  
        Random random = new Random();
        inGame = true;
        minesLeft = mines;
  
        allCells = rows * cols;
        field = new int[allCells];
  
        for (int i = 0; i < allCells; i++) {
          field[i] = 10;
        }
  
        statusbar.setText(Integer.toString(minesLeft));
  
        int i = 0;
  
        while (i < mines) {
          int position = (int) (allCells * random.nextDouble());

          if ((position < allCells) && (field[position] != 19)) {
            int current_col = position % cols;
            field[position] = 19;
            i++;

            if (current_col > 0) {
              cell = position - 1 - cols;
              if (cell >= 0) {
                if (field[cell] != 19) {
                  field[cell] += 1;
                }
              }

              cell = position - 1;
              if (cell >= 0) {
                if (field[cell] != 19) {
                  field[cell] += 1;
                }
              }

              cell = position + cols - 1;
              if (cell < allCells) {
                if (field[cell] != 19) {
                  field[cell] += 1;
                }
              }
            }

            cell = position - cols;
            if (cell >= 0) {
              if (field[cell] != 19) {
                field[cell] += 1;
              }
            }

            cell = position + cols;
            if (cell < allCells) {
              if (field[cell] != 19) {
                field[cell] += 1;
              }
            }

            if (current_col < (cols - 1)) {
              cell = position - cols + 1;
              if (cell >= 0) {
                if (field[cell] != 19) {
                  field[cell] += 1;
                }
              }

              cell = position + cols + 1;
              if (cell < allCells) {
                if (field[cell] != 19) {
                  field[cell] += 1;
                }
              }

              cell = position + 1;
              if (cell < allCells) {
                if (field[cell] != 19) {
                  field[cell] += 1;
                }
              }
            }
          }
        }
      }
      
      private void playExplosion() throws Exception { 
        File file = new File("explosion.au");
        Clip clip2 = AudioSystem.getClip();
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);

        clip2.open(ais);
        clip2.start();
      }

      private void findEmptyCells(int j) {
        int cols = Minesweeper.Globals.cols;
        int current_col = j % cols;
        int cell;
  
        if (current_col > 0) {
          cell = j - cols - 1;
          if (cell >= 0) {
            if (field[cell] > 9) {
              field[cell] -= 10;

              if (field[cell] == 0) {
                findEmptyCells(cell);
              }
            }
          }

          cell = j - 1;
          if (cell >= 0) {
            if (field[cell] > 9) {
              field[cell] -= 10;

              if (field[cell] == 0) {
                findEmptyCells(cell);
              }
            }
          }

          cell = j + cols - 1;
          if (cell < allCells) {
            if (field[cell] > 9) {
              field[cell] -= 10;

              if (field[cell] == 0) {
                findEmptyCells(cell);
              }
            }
          }
        }
  
        cell = j - cols;
        if (cell >= 0) {
          if (field[cell] > 9) {
            field[cell] -= 10;

            if (field[cell] == 0) {
              findEmptyCells(cell);
            }
          }
        }
  
        cell = j + cols;
        if (cell < allCells) {
          if (field[cell] > 9) {
            field[cell] -= 10;

            if (field[cell] == 0) {
              findEmptyCells(cell);
            }
          }
        }
  
        if (current_col < (cols - 1)) {
          cell = j - cols + 1;
          if (cell >= 0) {
            if (field[cell] > 9) {
              field[cell] -= 10;

              if (field[cell] == 0) {
                findEmptyCells(cell);
              }
            }
          }

          cell = j + cols + 1;
          if (cell < allCells) {
            if (field[cell] > 9) {
              field[cell] -= 10;

              if (field[cell] == 0) {
                findEmptyCells(cell);
              }
            }
          }

          cell = j + 1;
          if (cell < allCells) {
            if (field[cell] > 9) {
              field[cell] -= 10;

              if (field[cell] == 0) {
                findEmptyCells(cell);
              }
            }
          }
        }
      }
    
      @Override
      public void paintComponent(Graphics g) {
        int uncover = 0;
        int cols = Minesweeper.Globals.cols;
        int rows = Minesweeper.Globals.rows;
  
        for (int i = 0; i < rows; i++) {
          for (int j = 0; j < cols; j++) {
            int cell = field[(i * cols) + j];

            if (inGame && cell == 9) {
              inGame = false;
            }

            if (!inGame) {
              if (cell == 19) {
                cell = 9;
              } else if (cell == 29) {
                cell = 11;
              } else if (cell > 19) {
                cell = 12;
              } else if (cell > 9) {
                cell = 10;
              }
            } else {
              if (cell > 19) {
                cell = 11;
              } else if (cell > 9) {
                cell = 10;
                uncover++;
              }
            }

            g.drawImage(img[cell], (j * 22), (i * 22), this);
          }
        }
  
        if (uncover == 0 && inGame) {
          inGame = false;
          statusbar.setText("Game won");
        } else if (!inGame) {
          statusbar.setText("Game lost");
        }
      }
    
      private class MinesAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
          int x = e.getX();
          int y = e.getY();

          int cCol = x / 22;
          int cRow = y / 22;

          boolean doRepaint = false;

          if (!inGame) {
            setVisible(false);

            int option = startGameDialog();

            if (option == 3) {
              Minesweeper.Globals.cols = 7;
              Minesweeper.Globals.rows = 9;
              Minesweeper.Globals.width = 7 * 22 + 1;
              Minesweeper.Globals.height = 9 * 22 + 1;
              Minesweeper.Globals.mines = 10;
            } else if (option == 2) {
              Minesweeper.Globals.cols = 13;
              Minesweeper.Globals.rows = 18;
              Minesweeper.Globals.width = 13 * 22 + 1;
              Minesweeper.Globals.height = 18 * 22 + 1;
              Minesweeper.Globals.mines = 35;
            } else if (option == 1) {
              Minesweeper.Globals.cols = 22;
              Minesweeper.Globals.rows = 25;
              Minesweeper.Globals.width = 22 * 22 + 1;
              Minesweeper.Globals.height = 25 * 22 + 1;
              Minesweeper.Globals.mines = 91;
            } else {
              System.exit(0);
            }

            EventQueue.invokeLater(() -> {
              initUI();
            });
          }

          if ((x < Minesweeper.Globals.cols * 22) && (y < Minesweeper.Globals.rows * 22)) {
            if (e.getButton() == MouseEvent.BUTTON3) {
              if (field[(cRow * Minesweeper.Globals.cols) + cCol] > 9) {
                doRepaint = true;

                if (field[(cRow * Minesweeper.Globals.cols) + cCol] <= 19) {
                  if (minesLeft > 0) {
                    field[(cRow * Minesweeper.Globals.cols) + cCol] += 10;
                    minesLeft--;
                    String msg = Integer.toString(minesLeft);
                    statusbar.setText(msg);
                  } else {
                    statusbar.setText("No marks left");
                  }
                } else {
                  field[(cRow * Minesweeper.Globals.cols) + cCol] -= 10;
                  minesLeft++;
                  String msg = Integer.toString(minesLeft);
                  statusbar.setText(msg);
                }
              }
            } else {
              if (field[(cRow * Minesweeper.Globals.cols) + cCol] > 19) {
                return;
              }

              if ((field[(cRow * Minesweeper.Globals.cols) + cCol] > 9)
                  && (field[(cRow * Minesweeper.Globals.cols) + cCol] < 29)) {

                field[(cRow * Minesweeper.Globals.cols) + cCol] -= 10;
                doRepaint = true;

                if (field[(cRow * Minesweeper.Globals.cols) + cCol] == 9) {
                  try {
                    playExplosion();
                  } catch (Exception exception) {
                    System.out.println(exception);
                  }
                  
                  inGame = false;
                }

                if (field[(cRow * Minesweeper.Globals.cols) + cCol] == 0) {
                  findEmptyCells((cRow * Minesweeper.Globals.cols) + cCol);
                }
              }
            }

              if (doRepaint) {
                repaint();
              }
          }
        }
      }
    }

    public Minesweeper() {
      initUI();
    }

    private void initUI() {
      statusbar = new JLabel("");
      add(statusbar, BorderLayout.SOUTH);

      add(new Board(statusbar));

      setResizable(false);
      pack();

      setTitle("Minesweeper");
      setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static Integer startGameDialog() {
      Object[] options = {"Exit", "Expert", "Advanced", "Beginner"};
  
      int n = JOptionPane.showOptionDialog(
        null,
        "What level would you like to play?",
        "Minesweeper",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[0]
      );
  
      return n;
    }

    public static void main(String[] args) {
      int option = startGameDialog();

      if (option == 3) {
        Minesweeper.Globals.cols = 7;
        Minesweeper.Globals.rows = 9;
        Minesweeper.Globals.width = 7 * 22 + 1;
        Minesweeper.Globals.height = 9 * 22 + 1;
        Minesweeper.Globals.mines = 10;
      } else if (option == 2) {
        Minesweeper.Globals.cols = 13;
        Minesweeper.Globals.rows = 18;
        Minesweeper.Globals.width = 13 * 22 + 1;
        Minesweeper.Globals.height = 18 * 22 + 1;
        Minesweeper.Globals.mines = 35;
      } else if (option == 1) {
        Minesweeper.Globals.cols = 22;
        Minesweeper.Globals.rows = 25;
        Minesweeper.Globals.width = 22 * 22 + 1;
        Minesweeper.Globals.height = 25 * 22 + 1;
        Minesweeper.Globals.mines = 91;
      } else {
        System.exit(0);
      }

      EventQueue.invokeLater(() -> {
        Minesweeper m = new Minesweeper();
        m.setVisible(true);
      });
    }
}
