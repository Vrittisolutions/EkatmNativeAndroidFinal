uniform highp mat4 matProj;
uniform highp mat4 matWorld;
uniform highp mat4 matWorldView;
uniform highp vec4 ClipPlanes[6];
uniform highp vec4 DataPositionOffset;
uniform highp vec4 DisplacementAxesConstraints;
uniform sampler2D HeightMapTexture;
attribute highp vec4 vPosition;
attribute highp vec4 vTexCoord0;
attribute highp vec3 vNormal;
varying highp vec4 xlv_TEXCOORD0;
varying highp vec4 xlv_TEXCOORD1;
varying highp vec3 xlv_TEXCOORD2;
varying highp float xlv_TEXCOORD6;
varying highp vec4 xlv_TEXCOORD7;
varying highp vec4 xlv_COLOR4;
varying highp vec2 xlv_COLOR5;
void main ()
{
  highp vec4 tmpvar_1;
  tmpvar_1 = vPosition;
  highp mat4 vMatWorldViewProj_2;
  highp vec4 tmpvar_3;
  highp vec4 tmpvar_4;
  highp vec3 tmpvar_5;
  highp vec4 tmpvar_6;
  highp vec2 tmpvar_7;
  vMatWorldViewProj_2 = (matWorldView * matProj);
  highp vec3 tmpvar_8;
  highp vec3 offset_9;
  offset_9 = DataPositionOffset.xyz;
  if ((((
    (vTexCoord0.x <= -0.1)
   || 
    (vTexCoord0.x >= 1.1)
  ) || (vTexCoord0.y <= -0.1)) || (vTexCoord0.y >= 1.1))) {
    tmpvar_8 = DataPositionOffset.xyz;
  } else {
    highp vec3 tmpvar_10;
    if (bool(DisplacementAxesConstraints.w)) {
      tmpvar_10 = DisplacementAxesConstraints.xyz;
    } else {
      tmpvar_10 = (normalize(vPosition.xyz) * DisplacementAxesConstraints.xyz);
    };
    lowp vec4 tmpvar_11;
    tmpvar_11 = texture2DLod (HeightMapTexture, vTexCoord0.xy, 0.0);
    highp float tmpvar_12;
    tmpvar_12 = tmpvar_11.x;
    offset_9 = (DataPositionOffset.xyz + (tmpvar_10 * vec3(tmpvar_12)));
    tmpvar_8 = offset_9;
  };
  tmpvar_1.xyz = (vPosition.xyz + tmpvar_8);
  highp mat3 tmpvar_13;
  tmpvar_13[0] = matWorld[0].xyz;
  tmpvar_13[1] = matWorld[1].xyz;
  tmpvar_13[2] = matWorld[2].xyz;
  tmpvar_5 = (vNormal * tmpvar_13);
  tmpvar_4 = tmpvar_1;
  highp float tmpvar_14;
  tmpvar_14 = sqrt(dot (tmpvar_1.xyz, tmpvar_1.xyz));
  highp vec4 tmpvar_15;
  tmpvar_15 = (tmpvar_1 * matWorld);
  bvec4 tmpvar_16;
  tmpvar_16 = bvec4(ClipPlanes[0]);
  bool tmpvar_17;
  tmpvar_17 = any(tmpvar_16);
  highp float tmpvar_18;
  if (tmpvar_17) {
    highp vec4 tmpvar_19;
    tmpvar_19.w = 1.0;
    tmpvar_19.xyz = tmpvar_15.xyz;
    tmpvar_18 = dot (tmpvar_19, ClipPlanes[0]);
  } else {
    tmpvar_18 = 1.0;
  };
  tmpvar_6.x = tmpvar_18;
  bvec4 tmpvar_20;
  tmpvar_20 = bvec4(ClipPlanes[1]);
  bool tmpvar_21;
  tmpvar_21 = any(tmpvar_20);
  highp float tmpvar_22;
  if (tmpvar_21) {
    highp vec4 tmpvar_23;
    tmpvar_23.w = 1.0;
    tmpvar_23.xyz = tmpvar_15.xyz;
    tmpvar_22 = dot (tmpvar_23, ClipPlanes[1]);
  } else {
    tmpvar_22 = 1.0;
  };
  tmpvar_6.y = tmpvar_22;
  bvec4 tmpvar_24;
  tmpvar_24 = bvec4(ClipPlanes[2]);
  bool tmpvar_25;
  tmpvar_25 = any(tmpvar_24);
  highp float tmpvar_26;
  if (tmpvar_25) {
    highp vec4 tmpvar_27;
    tmpvar_27.w = 1.0;
    tmpvar_27.xyz = tmpvar_15.xyz;
    tmpvar_26 = dot (tmpvar_27, ClipPlanes[2]);
  } else {
    tmpvar_26 = 1.0;
  };
  tmpvar_6.z = tmpvar_26;
  bvec4 tmpvar_28;
  tmpvar_28 = bvec4(ClipPlanes[3]);
  bool tmpvar_29;
  tmpvar_29 = any(tmpvar_28);
  highp float tmpvar_30;
  if (tmpvar_29) {
    highp vec4 tmpvar_31;
    tmpvar_31.w = 1.0;
    tmpvar_31.xyz = tmpvar_15.xyz;
    tmpvar_30 = dot (tmpvar_31, ClipPlanes[3]);
  } else {
    tmpvar_30 = 1.0;
  };
  tmpvar_6.w = tmpvar_30;
  bvec4 tmpvar_32;
  tmpvar_32 = bvec4(ClipPlanes[4]);
  bool tmpvar_33;
  tmpvar_33 = any(tmpvar_32);
  highp float tmpvar_34;
  if (tmpvar_33) {
    highp vec4 tmpvar_35;
    tmpvar_35.w = 1.0;
    tmpvar_35.xyz = tmpvar_15.xyz;
    tmpvar_34 = dot (tmpvar_35, ClipPlanes[4]);
  } else {
    tmpvar_34 = 1.0;
  };
  tmpvar_7.x = tmpvar_34;
  bvec4 tmpvar_36;
  tmpvar_36 = bvec4(ClipPlanes[5]);
  bool tmpvar_37;
  tmpvar_37 = any(tmpvar_36);
  highp float tmpvar_38;
  if (tmpvar_37) {
    highp vec4 tmpvar_39;
    tmpvar_39.w = 1.0;
    tmpvar_39.xyz = tmpvar_15.xyz;
    tmpvar_38 = dot (tmpvar_39, ClipPlanes[5]);
  } else {
    tmpvar_38 = 1.0;
  };
  tmpvar_7.y = tmpvar_38;
  tmpvar_1.w = 1.0;
  tmpvar_3.zw = vec2(0.0, 0.0);
  tmpvar_3.xy = vTexCoord0.xy;
  gl_Position = (tmpvar_1 * vMatWorldViewProj_2);
  xlv_TEXCOORD0 = tmpvar_3;
  xlv_TEXCOORD1 = tmpvar_4;
  xlv_TEXCOORD2 = tmpvar_5;
  xlv_TEXCOORD6 = tmpvar_14;
  xlv_TEXCOORD7 = vTexCoord0;
  xlv_COLOR4 = tmpvar_6;
  xlv_COLOR5 = tmpvar_7;
}

