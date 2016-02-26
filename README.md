# AndroidOthello
This is a little android game, Othello
## Othello (Android)
- Tools: Android Studio, using API level 23
- Test AVD: Nexus S (with resolution 800x480)

#### Game UI
(img)
#### Implementation of Game Rules
`//8 possible directions check`
`for(dx = -1; dx <= 1; dx++){`
`  for(dy = -1; dy <= 1; dy++){`
`	//empty direction`
`	if(dx == 0 && dy == 0)`
`	  continue;`
`	int pos_x, pos_y;`
`	for(int steps = 1; steps < SIZE; steps++){`
`	  pos_x = x + steps * dx;`
`	  pos_y = y + steps * dy;`
`	  //boundary check -> break`
`	  if(pos_x < 0 || pos_x > 7 || pos_y < 0 || pos_y > 7)`
`		break;`
`	  //find the opposite chess -> continue`
`	  if(steps == 1 && ChessBoard[pos_x][pos_y] != opp_player)`
`		break;`
`	  //find empty chess -> break`
`	  if(ChessBoard[pos_x][pos_y] == EMPTY)`
`		break;`
`	  //find the current chess -> Got it!`
`	  if(ChessBoard[pos_x][pos_y] == cur_player){`
`		if(steps > 1){`
`		  changeNum += (steps -1);`
`		  if(sureToMove)`
`			for (int backsteps = steps - 1; backsteps >= 0; backsteps--) {`
`			  ChessBoard[x + backsteps * dx][y + backsteps * dy] = cur_player;`
`			}`
`		}`
`	  break;`
`	  }`
`	}`
`  }`
`}`
`return changeNum;`
