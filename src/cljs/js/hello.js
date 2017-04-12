export var sayHello = function() {
    console.log("Hello, world!");
};
export var sayThings = function(xs) {
    for(let x of xs) {
        console.log(x);
    }
};
export var reactHello = function() {
    return <div>Hello world!</div>
};
