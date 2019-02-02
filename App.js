import React from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import FormControl from '@material-ui/core/FormControl';
import Input from '@material-ui/core/Input';
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import * as fetch from "node-fetch";
import campusMap from './campus_map.jpg';

class App extends React.Component {
    state = {
        buildings: [],
        startBuilding: "Building name",
        endBuilding: "",
        image: campusMap,
        list: ""
    };

    changeStart = event => {
        this.setState({startBuilding: event.target.value});
    }

    changeEnd = event => {
        this.setState({endBuilding: event.target.value});
    }

    submitInfo = event => {
        fetch('http://localhost:8080/buildingLocation?start=' + this.state.startBuilding + '&end=' + this.state.endBuilding
        ).then(res => {
            return res.json()
        }).then(j =>
            {
                this.setState({list: j})
            })
    }

    resetInfo = event => {
        this.setState(
            {startBuilding: "",
            endBuilding: "",
            list: ""}
        );
    }

    componentDidUpdate() {
        var can = this.refs.canvas;
        var ctx = can.getContext("2d");
        var img = this.refs.image;

        ctx.drawImage(img, 0, 0);

        for (var i = 0; i < this.state.list.length; i++) {
            var startX = this.state.list[i]["start"]["x"];
            var startY = this.state.list[i]["start"]["y"];
            var endX = this.state.list[i]["destination"]["x"];
            var endY = this.state.list[i]["destination"]["y"];

            if (i === 0) {
                ctx.beginPath();
                ctx.arc(startX, startY, 30, 0, 2 * Math.PI);
                ctx.stroke();
                ctx.fillStyle = "red";
                ctx.fill();
            }

            if (i === this.state.list.length - 1) {
                ctx.beginPath();
                ctx.arc(endX, endY, 30, 0, 2* Math.PI);
                ctx.stroke();
                ctx.fillStyle = "red";
                ctx.fill();
            }

            ctx.moveTo(Math.round(startX), Math.round(startY));
            ctx.lineTo(Math.round(endX), Math.round(endY));
            ctx.lineWidth=8;
            ctx.strokeStyle="blue";
            ctx.stroke();
        }
    }

    componentWillMount() {
        fetch('http://localhost:8080/getBuildings'
        ).then(res => {
            return res.json()
        }).then(j =>
        {
            this.setState({buildings: j})
        })
    }

    drawImage = () => {
        var can = this.refs.canvas;
        var ctx = can.getContext("2d");
        var img = this.refs.image;

        ctx.drawImage(img, 0, 0);
    }

    getLongName(building) {
        return building.longName;
    }

    render() {
        const {classes} = this.props;

        const buildingsList = this.state.buildings.map((building) =>
            <MenuItem value={building.shortName}>{building.longName}</MenuItem>
        );

        return (
            <form>
                <FormControl>
                    <InputLabel htmlFor="start">Start</InputLabel>
                    <Select
                        autoWidth={true}
                        value={this.state.startBuilding}
                        onChange={this.changeStart}
                        inputProps={{
                            name: 'Start',
                            id: 'start'
                        }}
                    >
                        {buildingsList}
                    </Select>
                </FormControl>
                <FormControl>
                    <InputLabel htmlFor="end">End</InputLabel>
                    <Select
                        autoWidth={true}
                        value={this.state.endBuilding}
                        onChange={this.changeEnd}
                        inputProps={{
                            name: 'End',
                            id: 'end'
                        }}
                    >
                        {buildingsList}
                    </Select>
                </FormControl>
                <Button
                    onClick={this.submitInfo}
                >
                    Submit
                </Button>
                <Button
                    onClick={this.resetInfo}
                >
                    Reset
                </Button>
                <canvas ref="canvas" width="4330" height="2964"></canvas>
                <img ref="image" src={campusMap} style={{display: "none"}}
                onLoad={this.drawImage}/>
            </form>
        );
    }
}

App.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default App;
