const roborally = (() => {
    /**
     *
     * @typedef  Board
     * @type {object}
     * @property {string} name      - The name of the board
     * @property {SpaceType[][]} spaces - The width of the board
     */

    /**
     * Enum for space types
     * @readonly
     * @enum {number}
     */
    const SpaceType = {
        EMPTY: 0,
        SLOW_BELT: 1,
        CHECKPOINT: 2,
        ROTATOR: 2,
    };


    /**
     *
     * @typedef  Space
     * @type {SlowBelt | Rotator | Empty}
     */

    /**
     * Enum for rotations
     * @readonly
     * @enum {number}
     */
    const Rotation = {
        CLOCKWISE: 0,
        COUNTERCLOCKWISE: 1,
    }

    /**
     * Enum for directions
     * @readonly
     * @enum {number}
     */
    const Heading = {
        NORTH: 0,
        EAST: 1,
        SOUTH: 2,
        WEST: 3,
    }

    /**
     * All kinds of spaces can have walls in 0 to 4 directions.
     * @typedef  Walls
     * @type {Heading[]}
     */

    /**
     * The belt can transport in 4 directions
     * @typedef  SlowBelt
     * @type {object}
     * @property {SpaceType} type
     * @property {Heading} direction
     */

    /**
     * The rotator can rotate in two directions
     * @typedef  Checkpoint
     * @type {object}
     * @property {SpaceType} type
     */

    /**
     * The rotator can rotate in two directions
     * @typedef  Rotator
     * @type {object}
     * @property {SpaceType} type
     * @property {Rotation} rotation
     */

    /**
     * The rotator can rotate in two directions
     * @typedef  Empty
     * @type {object}
     * @property {SpaceType} type
     */

    /**
     * Creates a space object
     * @param type {SpaceType}
     * @param walls {Walls[]}
     * @param data {Heading | Rotation | {}}
     * @return {Space}
     */

    function createSpace(type, data = {}, walls = []) {
        return {
            type,
            walls,
            data,
        }
    }

    let gameContainer = document.getElementById('main-container');

    let pollFn = () => console.log('poll function is running, but not doing any http requests');

    /**
     * We set up the polling and return a function that quits the polling.
     * The polling function will poll whatever function we point the variable pollFn to.
     * @return {function(): void} - A function that stops the polling
     */
    function setupPolling() {
        let intervalId = setInterval(() => pollFn(), 1000);
        return () => clearInterval(intervalId);
    }

    /**
     * hides all elements with the given query selector
     * @param querySelectorArr {string[]}
     */
    function hide(querySelectorArr) {
        querySelectorArr.forEach(sel =>
            document.querySelectorAll(sel).forEach(el => el.classList.add('hidden')))
    }


    /**
     * creates a new game
     * @return {Promise<void>}
     */
    async function newGame() {
        // This is just for creating frontend before we have backend.
        // This will either be an HTTP request,
        // or we will just use a template engine to render board alternatives at first request.
        // const stopPolling = setupPolling();
        // stopPolling();
        hide(['#new-game-btn', '#number-of-players']);
        pollFn = () => {
            console.log('poll function is running, but not doing any http requests. we should currently be polling the backend to see if anyone is joining');
        };

        const boards = await getBoardAlternatives()

        for (const board of boards) {
            let div = document.createElement('div');
            div.classList.add('board-alternative');
            let table = document.createElement('table');

            console.table(board)
            addToTable(table, 'Board name', board['name']);
            addToTable(table, 'Width', board.spaces.length);
            addToTable(table, 'Height', board.spaces[0].length);
            div.appendChild(table);

            const btn = document.createElement('button');
            btn.textContent = 'Pick';
            btn.addEventListener('click', () => {
                pickBoard(board);
            });
            div.appendChild(btn);
            gameContainer.appendChild(div);
        }
    }

    function addToTable(table, headerText, dataText) {
        let row = document.createElement('tr');
        let header = document.createElement('th');
        let data = document.createElement('td');
        header.innerText = headerText;
        data.innerText = dataText;
        row.appendChild(header);
        row.appendChild(data);
        table.appendChild(row);
    }

    /**
     *
     * drawBoard - Draws a board on the screen
     * @param {Board} board
     */
    function pickBoard(board) {
        // Setup polling the backend for players joining.
        // console.log(`picked the board called ${board.name}`);
        // console.log('0 = empty, 1 = belt, 2 = checkpoint')
        // console.table(board.spaces);
        drawBoard(board);

    }

    /**
     *
     * @param rowDiv {HTMLDivElement}
     * @param space {Space}
     */
    function appendSpaceSpan(rowDiv, space) {
        const spaceSpan = document.createElement('span');
        switch (space.type) {
            case SpaceType.EMPTY:
                spaceSpan.classList.add('tile', 'empty')
                break;
            case SpaceType.SLOW_BELT:
                spaceSpan.classList.add('tile', 'slow-belt')
                break;
            case SpaceType.CHECKPOINT:
                spaceSpan.classList.add('tile', 'checkpoint')
                break;
            case SpaceType.ROTATOR:
                spaceSpan.classList.add('tile', 'rotator')
                break;
            default:
                console.error("Unknown tile encountered: " + space)
                break;
        }
        rowDiv.appendChild(spaceSpan);
    }

    /**
     * drawBoard - Draws a board on the screen
     * @param {Board} board
     */
    function drawBoard(board) {
        gameContainer.innerHTML = '';
        // console.log("drawing board")
        const boardDiv = document.createElement('div');
        boardDiv.classList.add('board');
        board.spaces.forEach(row => {
            const rowDiv = document.createElement('div');
            rowDiv.classList.add('row');
            row.forEach(space => {
                appendSpaceSpan(rowDiv, space)
            })
            boardDiv.appendChild(rowDiv);
        })
        gameContainer.appendChild(boardDiv);
    }

    /**
     * Makes an HTTP request to our server to get the board alternatives
     * TODO implement http calls to our server
     * @return {Promise<Board[]>}
     */
    async function getBoardAlternatives() {
        const res = await fetch("http://localhost:8080/board");
        const data = await res.json();
        console.log(data)
    }

    async function findMistakes() {
        // console.log('dbg finding mistakes')
        // newGame().then();
        let boards = await getBoardAlternatives()
        console.log(boards);
        // await pickBoard(boards[0]);
    }

    findMistakes().then();

    return {
        newGame
    };
})();