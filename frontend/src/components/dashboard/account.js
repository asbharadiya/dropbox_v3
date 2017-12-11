import React, { Component } from 'react';
import {connect} from 'react-redux';
import NotificationSystem from 'react-notification-system';
import moment from 'moment';
import RightContent from './rightcontent';
import * as actions from '../../actions/user';


class Account extends Component {

	constructor(props){
		super(props);
    this.state = {
      firstName:"",
      lastName:"",
      about:"",
      email:"",
      contactNo:"",
      education:"",
      occupation:"",
      firstNameError:"",
      lastNameError:""
    }
    this.updateUserProfile = this.updateUserProfile.bind(this);
    this.handleChange = this.handleChange.bind(this);
    this.notificationSystem = null;
	}

  componentDidMount(){
    this.props.getUserProfile();
    this.props.getUserActivity();
    this.notificationSystem = this.refs.notificationSystem;
  }

  componentWillReceiveProps(nextProps,props){
    if(nextProps.updateUserProfileSuccess){
      this.notificationSystem.addNotification({
        message: 'Profile successfully updated',
        level: 'success'
      });
      this.props.getUserProfile();
    } else if(nextProps.updateUserProfileSuccess === false){
      this.notificationSystem.addNotification({
        message: 'Opps! Something went wrong',
        level: 'error'
      });
    }
    if(nextProps.profile){
      this.setState({
        firstName:nextProps.profile.firstName,
        lastName:nextProps.profile.lastName,
        about:nextProps.profile.about ? nextProps.profile.about:"",
        email:nextProps.profile.email,
        contactNo:nextProps.profile.contactNo ? nextProps.profile.contactNo:"",
        education:nextProps.profile.education ? nextProps.profile.education:"",
        occupation:nextProps.profile.occupation ? nextProps.profile.occupation:""
      });
    }
  }

  handleChange(event){
    this.setState({
      [event.target.name]:event.target.value
    });
  }

  updateUserProfile(){
    //update user profile
    if(this.state.first_name === ""){
      this.setState({
        firstNameError:"This field is required"
      });
      return;
    }
    if(this.state.last_name === ""){
      this.setState({
        lastNameError:"This field is required"
      });
      return;
    }
    this.props.updateUserProfile({
      firstName:this.state.firstName,
      lastName:this.state.lastName,
      about:this.state.about,
      contactNo:this.state.contactNo,
      education:this.state.education,
      occupation:this.state.occupation
    });
  }

	render() {
    return (
    		<div className="inner-page-content has-right-content">
    			<div className="accountspage">
    				<div className="page-header">
	    			  Basic info
	    		  </div>
            <div className="row">
              <div className="form-group col-xs-12 col-sm-6">
                <label>First name</label>
                <input type="text" className="form-control" name="firstName" value={this.state.firstName} onChange={this.handleChange}/>
              </div>
              <div className="form-group col-xs-12 col-sm-6">
                <label>Last name</label>
                <input type="text" className="form-control" name="lastName" value={this.state.lastName} onChange={this.handleChange}/>
              </div>
            </div>
            <div className="row">
              <div className="form-group col-xs-12">
                <label>About</label>
                <textarea className="form-control" name="about" value={this.state.about} onChange={this.handleChange}></textarea>
              </div>
            </div>
            <div className="row">
              <div className="form-group col-xs-12">
                <label>Email</label>
                <div>{this.state.email}</div>
              </div>
            </div>
            <div className="row">
              <div className="form-group col-xs-12">
                <label>Contact number</label>
                <input type="text" className="form-control" name="contactNo" value={this.state.contactNo} onChange={this.handleChange}/>
              </div>
            </div>
            <div className="row">
              <div className="form-group col-xs-12">
                <label>Education</label>
                <select className="form-control" name="education" value={this.state.education} onChange={this.handleChange}>
                  <option value="">Please select</option>
                  <option value="High School Diploma">High school diploma</option>
                  <option value="Bachelors Degree">Bachelor's degree</option>
                  <option value="Masters Degree">Master's degree</option>
                  <option value="PhD">Ph.D.</option>
                </select>
              </div>
            </div>
            <div className="row">
              <div className="form-group col-xs-12">
                <label>Occupation</label>
                <select className="form-control" name="occupation" value={this.state.occupation} onChange={this.handleChange}>
                  <option value="">Please select</option>
                  <option value="Employed">Employed</option>
                  <option value="Self-employed">Self-employed</option>
                  <option value="Student">Student</option>
                </select>
              </div>
            </div>
            <div className="row">
              <div className="form-group col-xs-12">
                <button className="btn btn-primary btn-dropbox" onClick={this.updateUserProfile}>Save</button>
              </div>
            </div>
            <div className="page-header">
              Activity report
            </div>
            <div className="row">
              <div className="col-xs-12">
                <ul className="activity-list">
                  {
                    this.props.activity.length > 0 ? (
                      this.props.activity.map(function(item,index) {
                        return (
                          <li className="activity-item" key={index}>
                            <span>{item.action} at {moment(item.createdDate).format('MM-DD-YYYY')}</span>
                          </li>
                        );
                      })
                    ) : (
                      <li className="activity-item">
                        <span>No activity</span>
                      </li> 
                    )
                  }
                </ul>
              </div>
            </div>
      		</div>
      		<RightContent pagetype="account"/>
          <NotificationSystem ref="notificationSystem" />
    		</div>
  	);
	}
}

function mapStateToProps(state) {
    return {
        profile:state.profile,
        activity:state.activity,
        updateUserProfileSuccess:state.updateUserProfileSuccess
    };
}

function mapDispatchToProps(dispatch) {
    return {
        getUserProfile : () => dispatch(actions.getUserProfile()),
        getUserActivity : () => dispatch(actions.getUserActivity()),
        updateUserProfile : (payload) => dispatch(actions.updateUserProfile(payload))
    };
}

export default connect(mapStateToProps,mapDispatchToProps)(Account);
