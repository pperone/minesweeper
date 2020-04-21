import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Date;
import java.io.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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
      private boolean in_game;
      private boolean winner;
      private int mines_left;
      private Image[] img;
    
      private int all_cells;
      private final JLabel statusbar;

      private final int BASE_BUTTON = 10;
      private final int EMPTY_BUTTON = 0;
      private final int MINE_BUTTON = 9;
      private final int MARK_BUTTON = 11;
      private final int WRONG_MARK_BUTTON = 12;
      private final int COVERED_MINE_BUTTON = 19;
      private final int MARKED_MINE_BUTTON = 29;
    
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
  
        addMouseListener(new Mines());
        newGame(new Date());
      }

      private void newGame(Date start) {
        int cell;
        int mines = Minesweeper.Globals.mines;
        int cols = Minesweeper.Globals.cols;
        int rows = Minesweeper.Globals.rows;

        Random random = new Random();
        in_game = true;
        mines_left = mines;
  
        all_cells = rows * cols;
        field = new int[all_cells];
  
        for (int i = 0; i < all_cells; i++) {
          field[i] = BASE_BUTTON;
        }
  
        int i = 0;
  
        while (i < mines) {
          int position = (int) (all_cells * random.nextDouble());

          if ((position < all_cells) && (field[position] != COVERED_MINE_BUTTON)) {
            int current_col = position % cols;
            field[position] = COVERED_MINE_BUTTON;
            i++;

            if (current_col > 0) {
              cell = position - 1 - cols;
              if (cell >= 0) {
                if (field[cell] != COVERED_MINE_BUTTON) {
                  field[cell] += 1;
                }
              }

              cell = position - 1;
              if (cell >= 0) {
                if (field[cell] != COVERED_MINE_BUTTON) {
                  field[cell] += 1;
                }
              }

              cell = position + cols - 1;
              if (cell < all_cells) {
                if (field[cell] != COVERED_MINE_BUTTON) {
                  field[cell] += 1;
                }
              }
            }

            cell = position - cols;
            if (cell >= 0) {
              if (field[cell] != COVERED_MINE_BUTTON) {
                field[cell] += 1;
              }
            }

            cell = position + cols;
            if (cell < all_cells) {
              if (field[cell] != COVERED_MINE_BUTTON) {
                field[cell] += 1;
              }
            }

            if (current_col < (cols - 1)) {
              cell = position - cols + 1;
              if (cell >= 0) {
                if (field[cell] != COVERED_MINE_BUTTON) {
                  field[cell] += 1;
                }
              }

              cell = position + cols + 1;
              if (cell < all_cells) {
                if (field[cell] != COVERED_MINE_BUTTON) {
                  field[cell] += 1;
                }
              }

              cell = position + 1;
              if (cell < all_cells) {
                if (field[cell] != COVERED_MINE_BUTTON) {
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
            if (field[cell] > MINE_BUTTON) {
              field[cell] -= BASE_BUTTON;

              if (field[cell] == EMPTY_BUTTON) {
                findEmptyCells(cell);
              }
            }
          }

          cell = j - 1;
          if (cell >= 0) {
            if (field[cell] > MINE_BUTTON) {
              field[cell] -= BASE_BUTTON;

              if (field[cell] == EMPTY_BUTTON) {
                findEmptyCells(cell);
              }
            }
          }

          cell = j + cols - 1;
          if (cell < all_cells) {
            if (field[cell] > MINE_BUTTON) {
              field[cell] -= BASE_BUTTON;

              if (field[cell] == EMPTY_BUTTON) {
                findEmptyCells(cell);
              }
            }
          }
        }
  
        cell = j - cols;
        if (cell >= 0) {
          if (field[cell] > MINE_BUTTON) {
            field[cell] -= BASE_BUTTON;

            if (field[cell] == EMPTY_BUTTON) {
              findEmptyCells(cell);
            }
          }
        }
  
        cell = j + cols;
        if (cell < all_cells) {
          if (field[cell] > MINE_BUTTON) {
            field[cell] -= BASE_BUTTON;

            if (field[cell] == EMPTY_BUTTON) {
              findEmptyCells(cell);
            }
          }
        }
  
        if (current_col < (cols - 1)) {
          cell = j - cols + 1;
          if (cell >= 0) {
            if (field[cell] > MINE_BUTTON) {
              field[cell] -= BASE_BUTTON;

              if (field[cell] == EMPTY_BUTTON) {
                findEmptyCells(cell);
              }
            }
          }

          cell = j + cols + 1;
          if (cell < all_cells) {
            if (field[cell] > MINE_BUTTON) {
              field[cell] -= BASE_BUTTON;

              if (field[cell] == EMPTY_BUTTON) {
                findEmptyCells(cell);
              }
            }
          }

          cell = j + 1;
          if (cell < all_cells) {
            if (field[cell] > MINE_BUTTON) {
              field[cell] -= BASE_BUTTON;

              if (field[cell] == EMPTY_BUTTON) {
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

            if (in_game && cell == MINE_BUTTON) {
              in_game = false;
            }

            if (!in_game) {
              if (cell == COVERED_MINE_BUTTON) {
                cell = MINE_BUTTON;
              } else if (cell == MARKED_MINE_BUTTON) {
                cell = MARK_BUTTON;
              } else if (cell > COVERED_MINE_BUTTON) {
                cell = WRONG_MARK_BUTTON;
              } else if (cell > MINE_BUTTON) {
                cell = BASE_BUTTON;
              }
            } else {
              if (cell > COVERED_MINE_BUTTON) {
                cell = MARK_BUTTON;
              } else if (cell > MINE_BUTTON) {
                cell = BASE_BUTTON;
                uncover++;
              }
            }

            g.drawImage(img[cell], (j * 22), (i * 22), this);
          }
        }
  
        if (uncover == 0 && in_game) {
          in_game = false;
          statusbar.setText("You won!");
        } else if (!in_game && winner) {
          statusbar.setText("You won!");
        } else {
          statusbar.setText("You lost...");
        }
      }
    
      private class Mines extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
          int x = e.getX();
          int y = e.getY();

          int cCol = x / 22;
          int cRow = y / 22;

          boolean doRepaint = false;

          if (!in_game) {
            setVisible(false);
            statusbar.setText("");

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
              if (field[(cRow * Minesweeper.Globals.cols) + cCol] > MINE_BUTTON) {
                doRepaint = true;

                if (field[(cRow * Minesweeper.Globals.cols) + cCol] <= COVERED_MINE_BUTTON) {
                  if (mines_left > 0) {
                    field[(cRow * Minesweeper.Globals.cols) + cCol] += BASE_BUTTON;
                    mines_left--;
                  }
                } else {
                  field[(cRow * Minesweeper.Globals.cols) + cCol] -= BASE_BUTTON;
                  mines_left++;
                }
              }
            } else {
              if (field[(cRow * Minesweeper.Globals.cols) + cCol] > COVERED_MINE_BUTTON) {
                return;
              }

              if ((field[(cRow * Minesweeper.Globals.cols) + cCol] > MINE_BUTTON)
                  && (field[(cRow * Minesweeper.Globals.cols) + cCol] < MARKED_MINE_BUTTON)) {

                field[(cRow * Minesweeper.Globals.cols) + cCol] -= BASE_BUTTON;
                doRepaint = true;

                if (field[(cRow * Minesweeper.Globals.cols) + cCol] == MINE_BUTTON) {
                  try {
                    playExplosion();
                  } catch (Exception exception) {
                    System.out.println(exception);
                  }
                  
                  in_game = false;
                  winner = true;
                }

                if (field[(cRow * Minesweeper.Globals.cols) + cCol] == EMPTY_BUTTON) {
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
      statusbar = new JLabel("", JLabel.CENTER);
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
