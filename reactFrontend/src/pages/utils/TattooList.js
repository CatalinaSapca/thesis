import React from "react";


import ToggleButton from 'react-toggle-button';

class TattooItem extends React.Component{

    handleUpdateThisTattoo=(event)=>{
        console.log('update button pentru '+this.props.tattooItem);
        this.props.handleUpdateThisTattoo(event, this.props.tattooItem);
    }

    handleDeleteThisTattoo=(event)=>{
        console.log('delete button pentru '+this.props.tattooItem.id);
        this.props.handleDeleteThisTattoo(event, this.props.tattooItem.id);
    }

    render() {
        return (
            // <tr>
            //     <td>{this.props.ar.id}</td>
            //     <td >{this.props.ar.artistName}</td>
            //     <td >{this.props.ar.data}</td>
            //     <td >{this.props.ar.location}</td>
            //     <td >{this.props.ar.availableSeats}</td>
            //     <td >{this.props.ar.soldSeats}</td>
            //     <td><button  onClick={this.handleDeleteThisTattoo}>Delete</button></td>
            // </tr>
            <section className="container mb-4">
            <head>
            <link rel="stylesheet" type="text/css" href="https://openai.com/assets/styles/all.css?v=6e4ea94c6b" />
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" />
            <link rel="stylesheet" href="styling.css" />
            </head>

            <section className="container mb-4">
                <div className="row">
                    <div className="order-2 order-md-1 col-12 col-md-7">
                        <div className="centering-tabs__content">
                            <div style={{borderRadius: '6px'}}>
                                <div className="center">
                                <img alt="logo" src= {this.props.tattooItem.path} style={{display: 'block', height: 'auto', border: 0, width: 's00px', maxWidth: '100%'}} title="img1" width={375} />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="order-1 order-md-2 col-12 col-md-5">
                        <div className="max-width-xxnarrow ml-md-auto">
                        <h2 > {this.props.tattooItem.description} </h2>
                        </div>

                        <div className="position-absolute" style={{position: 'absolute', right: 0, bottom: 80}}>
                        <label id="emailInputMessage" className="" style={{display:'block', color: 'grey', height: '10px', fontSize: '13px', float: 'right'}}> make public </label>
                        <br></br>
                        <ToggleButton
                                inactiveLabel={''} activeLabel={''} colors={{ activeThumb: { base: '#b812ff',},
                                inactiveThumb: { base: '#15032e', }, active: { base: '#56107a',  hover: '#a141d9',},
                                inactive: { base: '#ac9ab5', hover: '#9172a1' }
                                }}
                                style = {{float: 'right'}}
                                thumbAnimateRange={[-7, 32]}
                                value = {this.props.tattooItem.public ? true : false}
                                onToggle={this.handleUpdateThisTattoo} />
                        </div>

                        
                        <div className="position-absolute" style={{position: 'absolute', right: 0, bottom: 10}}>
                        <li className="d-inline-block"><a className="btn btn-padded btn-dark btn-circle m-0" href="/" onClick={this.handleDeleteThisTattoo}>delete this idea</a></li>
                        </div>

                    </div>
                </div> 
            </section>
            </section>
        );
    }
}

class TattooList extends React.Component{

    render(){



        var rows = [];
        var handleUpdateThisTattoo=this.props.handleUpdateThisTattoo;
        var handleDeleteThisTattoo=this.props.handleDeleteThisTattoo;
        console.log(this.props.tattooList);
        this.props.tattooList.forEach(function(tattooItem) {  
            rows.push(<TattooItem tattooItem={tattooItem} key={tattooItem.id} handleDeleteThisTattoo={handleDeleteThisTattoo} handleUpdateThisTattoo={handleUpdateThisTattoo} />);
        });




        return (
        // <div className="ARTable">

        //     <table className="center">
        //         <thead>
        //         <tr>
        //             <th>ID</th>
        //             <th>artist name</th>
        //             <th>data</th>
        //             <th>location</th>
        //             <th>available seats</th>
        //             <th>sold seats</th>
        //         </tr>
        //         </thead>
        //         <tbody>{rows}</tbody>
        //     </table>

        //     </div>

        <section className="container mb-4">
            {rows}
        </section>
        );
    }
}

export default TattooList;


//  <section className="container mb-4">
//             <div className="row">
//               <div className="order-2 order-md-1 col-12 col-md-7">
//                 <div className="centering-tabs__content">
//                   <div style={{borderRadius: '6px'}}>
//                     <div className="center">
//                       <img alt="logo" src="https://i.ibb.co/ZN2rQgc/4.jpg" style={{display: 'block', height: 'auto', border: 0, width: 's00px', maxWidth: '100%'}} title="img1" width={375} />
//                     </div>
//                   </div>
//                 </div>
//               </div>
//               <div className="order-1 order-md-2 col-12 col-md-5">
//                 <div className="max-width-xxnarrow ml-md-auto">
//                   <h2 className="mb-1 h1.5 balance-text">ultraviolet uniqued trippy wall ganesha psychedelic </h2>
//                 </div>
//               </div>
//             </div> 